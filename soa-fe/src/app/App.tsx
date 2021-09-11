import React, { ChangeEvent, useCallback, useMemo, useState } from 'react';
import '../App.css';
import { AppContainer } from './App.styled';
import { createTestProvider } from './provider/test.provider';
import { none, Option, some } from 'fp-ts/Option';
import { pipe } from 'fp-ts/function';
import { either, option } from 'fp-ts';
import { createHumanBeingProvider, HumanBeing } from './provider/human-being.provider';
import { Either } from 'fp-ts/Either';
import { HumanBeingFilterForm } from './component/HumanBeingFilterForm/human-being-filter-form.component';

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

export const App = () => {
	const [label, setLabel] = useState<Option<string>>(none);
	const provider = useMemo(() => createTestProvider(), []);
	const humanBeingProvider = useMemo(() => createHumanBeingProvider(), []);
	const [id, setId] = useState('');
	const [filter, setFilter] = useState('');
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

	const onClickGetAllHandler = useCallback(() => {
		humanBeingProvider.getAllHumans(filter).subscribe(handleResult);
	}, [provider, filter]);

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
			<HumanBeingFilterForm onFilterChange={setFilter} />
			<div>
				{pipe(
					label,
					option.getOrElse(() => 'No data for now...'),
				)}
			</div>
		</AppContainer>
	);
};
