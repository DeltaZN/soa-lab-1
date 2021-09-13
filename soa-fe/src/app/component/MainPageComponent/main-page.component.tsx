import React, { memo, useCallback, useContext, useEffect, useMemo, useState } from 'react';
import { HumanBeingFilterForm } from '../HumanBeingFilterForm/human-being-filter-form.component';
import { Sorting, SortOption } from '../SortComponent/sorting.component';
import { constVoid, pipe } from 'fp-ts/function';
import { either, option } from 'fp-ts';
import { Pagination } from '../PaginationComponent/pagination.component';
import { HumanBeing, PaginationResult } from '../../provider/human-being.provider';
import { context } from '../../App';
import { none, Option, some } from 'fp-ts/Option';

export interface MainPageProps {}

const sortingOptions: SortOption[] = [
	['id', 'ID'],
	['name', 'Name'],
	['coordinatesX', 'X coordinate'],
	['coordinatesY', 'Y coordinate'],
	['creationDate', 'Creation date'],
	['realHero', 'Real hero'],
	['hasToothpick', 'Has toothpick'],
	['impactSpeed', 'Impact speed'],
	['soundtrackName', 'Soundtrack name'],
	['minutesOfWaiting', 'Minutes of waiting'],
	['weaponType', 'Weapon type'],
	['carName', 'Car name'],
	['carCool', 'Car cool'],
];

export const MainPage = memo<MainPageProps>(props => {
	const [label, setLabel] = useState<Option<string>>(none);
	const [filter, setFilter] = useState('');
	const [sorting, setSorting] = useState('');
	const [pagination, setPagination] = useState('');

	const [paginationResult, setPaginationResult] = useState<PaginationResult>({
		currentPage: 0,
		totalPages: 0,
		totalItems: 0,
		humans: [],
	});

	const params = useMemo(() => [filter, sorting, pagination].filter(s => s.length).join('&'), [
		filter,
		sorting,
		pagination,
	]);

	const { humanBeingProvider } = useContext(context);

	const updateHandle = useCallback(
		(human: HumanBeing) => {
			humanBeingProvider.updateHuman(human).subscribe(constVoid);
		},
		[humanBeingProvider],
	);

	const onClickGetAllHandler = useCallback(() => {
		humanBeingProvider.getAllHumans(params.length ? `?${params}` : '').subscribe(res =>
			pipe(
				res,
				either.fold(
					e => setLabel(some(e.toString())),
					data => {
						setLabel(some(''));
						setPaginationResult(data);
					},
				),
			),
		);
	}, [humanBeingProvider, params]);

	useEffect(() => {
		onClickGetAllHandler();
	}, [sorting, pagination]);

	return (
		<div>
			<HumanBeingFilterForm onFilterChange={setFilter} applyFilter={onClickGetAllHandler} />
			<Sorting options={sortingOptions} onChange={setSorting} />
			<div>
				{pipe(
					label,
					option.getOrElse(() => 'No data for now...'),
				)}
			</div>
			<Pagination
				paginationResult={paginationResult}
				updatePagination={setPagination}
				updateHuman={updateHandle}
			/>
		</div>
	);
});
