import React, { useCallback, useMemo, useState } from 'react';
import '../App.css';
import { AppContainer } from './App.styled';
import { createTestProvider } from './provider/test.provider';
import { none, Option, some } from 'fp-ts/Option';
import { pipe } from 'fp-ts/function';
import { either, option } from 'fp-ts';
import { createHumanBeingProvider, HumanBeing } from './provider/human-being.provider';

// For further use
// export const BASE_URL = `${process.env.PUBLIC_URL || 'http://localhost:8080/soa_be_war_exploded'}`;

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
		cool: false,
	},
};

export const App = () => {
	const [label, setLabel] = useState<Option<string>>(none);
	const provider = useMemo(() => createTestProvider(), []);
	const humanBeingProvider = useMemo(() => createHumanBeingProvider(), []);
	const onClickCreateHandler = useCallback(() => {
		humanBeingProvider.createHuman(randomHuman).subscribe(res =>
			pipe(
				res,
				either.fold(
					e => setLabel(some(e.toString())),
					data => setLabel(some(JSON.stringify(data))),
				),
			),
		);
	}, [provider]);
	const onClickGetAllHandler = useCallback(() => {
		humanBeingProvider.getAllHumans().subscribe(res =>
			pipe(
				res,
				either.fold(
					e => setLabel(some(e.toString())),
					data => setLabel(some(JSON.stringify(data))),
				),
			),
		);
	}, [provider]);
	return (
		<AppContainer>
			<button onClick={onClickCreateHandler}>TestCreate!</button>
			<button onClick={onClickGetAllHandler}>GetAll!</button>
			<div>
				{pipe(
					label,
					option.getOrElse(() => 'No data for now...'),
				)}
			</div>
		</AppContainer>
	);
};
