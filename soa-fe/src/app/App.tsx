import React, { useCallback, useMemo, useState } from 'react';
import '../App.css';
import { AppContainer } from './App.styled';
import { createTestProvider } from './provider/test.provider';
import { none, Option, some } from 'fp-ts/Option';
import { pipe } from 'fp-ts/function';
import { either, option } from 'fp-ts';

export const BASE_URL = 'http://localhost:8080/soa_be_war_exploded';

export const App = () => {
	const [label, setLabel] = useState<Option<string>>(none);
	const provider = useMemo(() => createTestProvider(), []);
	const onClickHandler = useCallback(
		() =>
			provider.doTest().subscribe(res =>
				pipe(
					res,
					either.fold(
						e => setLabel(some(e.toString())),
						data => setLabel(some(data)),
					),
				),
			),
		[provider],
	);
	return (
		<AppContainer>
			<button onClick={onClickHandler}>Click me!</button>
			<div>
				{pipe(
					label,
					option.getOrElse(() => 'No data for now...'),
				)}
			</div>
		</AppContainer>
	);
};
