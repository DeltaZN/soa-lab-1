import React, { ChangeEvent, useCallback, useEffect, useMemo, useState } from 'react';
import '../App.css';
import { AppContainer } from './App.styled';
import { createTestProvider } from './provider/test.provider';
import { none, Option, some } from 'fp-ts/Option';
import { constVoid, pipe } from 'fp-ts/function';
import { either, option } from 'fp-ts';
import { createHumanBeingProvider, HumanBeing, PaginationResult } from './provider/human-being.provider';
import { Either } from 'fp-ts/Either';
import { HumanBeingFilterForm } from './component/HumanBeingFilterForm/human-being-filter-form.component';
import { Sorting, SortOption } from './component/SortComponent/sorting.component';
import { Pagination } from './component/PaginationComponent/pagination.component';

const randomHuman: HumanBeing = {
	id: 0,
	name: 'test',
	coordinates: { x: 1, y: 2 },
	creationDate: 'today',
	hasToothpick: true,
	realHero: false,
	impactSpeed: 1,
	soundtrackName: 'smells like...',
	minutesOfWaiting: 32,
	weaponType: 'SHOTGUN',
	car: {
		name: 'wolkswagen',
		cool: false,
	},
};

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

export const App = () => {
	const [label, setLabel] = useState<Option<string>>(none);
	const provider = useMemo(() => createTestProvider(), []);
	const humanBeingProvider = useMemo(() => createHumanBeingProvider(), []);
	const [paginationResult, setPaginationResult] = useState<PaginationResult>({
		currentPage: 0,
		totalPages: 0,
		totalItems: 0,
		humans: [],
	});
	const [id, setId] = useState('');
	const [filter, setFilter] = useState('');
	const [sorting, setSorting] = useState('');
	const [pagination, setPagination] = useState('');
	const handleInput = useCallback((e: ChangeEvent<HTMLInputElement>) => {
		setId(e.target.value);
	}, []);

	const handleResult = (res: Either<Error, any>) =>
		pipe(
			res,
			either.fold(
				e => setLabel(some(e.toString())),
				data => setLabel(some(JSON.stringify(data))),
			),
		);

	const onClickCreateHandler = useCallback(() => {
		humanBeingProvider.createHuman(randomHuman).subscribe(handleResult);
	}, [provider]);

	const params = useMemo(() => [filter, sorting, pagination].filter(s => s.length).join('&'), [
		filter,
		sorting,
		pagination,
	]);

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
	}, [provider, params]);

	useEffect(() => {
		onClickGetAllHandler();
	}, [sorting, pagination]);

	const onClickGetByIdHandler = useCallback(() => {
		humanBeingProvider.getHuman(parseInt(id, 10)).subscribe(handleResult);
	}, [provider, id]);

	const onClickDeleteByIdHandler = useCallback(() => {
		humanBeingProvider.deleteHuman(parseInt(id, 10)).subscribe(handleResult);
	}, [provider, id]);

	const onClickCountSoundtrackNameLess = useCallback(() => {
		humanBeingProvider.countAllSoundtrackNameLess(id).subscribe(handleResult);
	}, [provider, id]);

	const onClickFindMinutesOfWaitingLess = useCallback(() => {
		humanBeingProvider.findAllMinutesOfWaitingLess(parseInt(id, 10)).subscribe(handleResult);
	}, [provider, id]);

	const onClickDeleteMinutesOfWaitingEqual = useCallback(() => {
		humanBeingProvider.deleteAnyMinutesOfWaitingEqual(parseInt(id, 10)).subscribe(handleResult);
	}, [provider, id]);

	const updateHandle = useCallback(
		(human: HumanBeing) => {
			humanBeingProvider.updateHuman(human).subscribe(handleResult);
		},
		[provider, id],
	);

	return (
		<AppContainer>
			<div>
				<label>ID</label>
				<input onChange={handleInput} value={id} />
			</div>
			<button onClick={onClickCreateHandler}>TestCreate!</button>
			<button onClick={onClickGetAllHandler}>GetAll!</button>
			<button onClick={onClickGetByIdHandler}>GetId!</button>
			<button onClick={onClickDeleteByIdHandler}>DeleteId!</button>
			<button onClick={onClickCountSoundtrackNameLess}>CountSoundtrackNameLess!</button>
			<button onClick={onClickFindMinutesOfWaitingLess}>FindAllMinutesOfWaitingLess!</button>
			<button onClick={onClickDeleteMinutesOfWaitingEqual}>DeleteAnyMinutesOfWaitingEqual!</button>
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
		</AppContainer>
	);
};
