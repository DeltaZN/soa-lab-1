import React, { ChangeEvent, memo, useCallback, useEffect, useState } from 'react';
import { ALL_WEAPONS, Car, HumanBeing } from '../../provider/human-being.provider';
import { RowTextInput } from '../HumanBeingTable/human-being-table.styled';
import { FilterRow } from '../HumanBeingFilterForm/human-being-filter-form.styled';
import { KeysOfType } from '../HumanBeingTable/human-being-table.component';
import { Option, some } from 'fp-ts/Option';
import { constNull, constVoid, pipe } from 'fp-ts/function';
import { option } from 'fp-ts';

export interface HumanBeingCreationFormProps {
	readonly human: Option<HumanBeing>;
	readonly onSave: (human: HumanBeing) => void;
}

export const HumanBeingCreationForm = memo<HumanBeingCreationFormProps>(props => {
	const { human, onSave } = props;
	const [state, setState] = useState<Option<HumanBeing>>(human);
	const [isEdit, setIsEdit] = useState<boolean>(false);

	useEffect(() => {
		setState(human);
	}, [human]);

	const handleSave = useCallback(() => {
		setIsEdit(false);
		pipe(state, option.fold(constVoid, onSave));
	}, [state, onSave]);
	const handleCancel = useCallback(() => {
		setState(human);
		setIsEdit(false);
	}, [human]);
	const handleEdit = useCallback(() => {
		setIsEdit(true);
	}, []);

	const handleTextHumanChange = useCallback(
		(key: KeysOfType<HumanBeing, number | string>) => (e: ChangeEvent<HTMLInputElement | HTMLSelectElement>) =>
			pipe(
				state,
				option.fold(constVoid, state => setState(some({ ...state, [key]: e.target.value }))),
			),
		[state],
	);

	const handleCoordsChange = useCallback(
		(key: 'x' | 'y') => (e: ChangeEvent<HTMLInputElement | HTMLSelectElement>) =>
			pipe(
				state,
				option.fold(constVoid, state =>
					setState(some({ ...state, coordinates: { ...state.coordinates, [key]: e.target.value } })),
				),
			),
		[state],
	);

	const handleBoolHumanChange = useCallback(
		(key: KeysOfType<HumanBeing, boolean>) => () =>
			pipe(
				state,
				option.fold(constVoid, state => setState(some({ ...state, [key]: !state[key] }))),
			),
		[state],
	);

	const handleTextCarChange = useCallback(
		(key: keyof Car) => (e: ChangeEvent<HTMLInputElement | HTMLSelectElement>) =>
			pipe(
				state,
				option.fold(constVoid, state =>
					setState(some({ ...state, car: { ...state.car, [key]: e.target.value } })),
				),
			),
		[state],
	);

	const handleBoolCarChange = useCallback(
		(key: keyof Car) => () =>
			pipe(
				state,
				option.fold(constVoid, state =>
					setState(some({ ...state, car: { ...state.car, [key]: !state.car[key] } })),
				),
			),
		[state],
	);

	return (
		<div>
			{pipe(
				state,
				option.fold(constNull, state => (
					<>
						<FilterRow>
							<div>ID: </div>
							<div>{state.id}</div>
						</FilterRow>
						<FilterRow>
							<div>Name: </div>
							<RowTextInput
								disabled={!isEdit}
								value={state.name}
								onChange={handleTextHumanChange('name')}
							/>
						</FilterRow>
						<FilterRow>
							<div>X: </div>
							<RowTextInput
								disabled={!isEdit}
								value={state.coordinates.x}
								onChange={handleCoordsChange('x')}
							/>
						</FilterRow>
						<FilterRow>
							<div>Y: </div>
							<RowTextInput
								disabled={!isEdit}
								value={state.coordinates.y}
								onChange={handleCoordsChange('y')}
							/>
						</FilterRow>
						<FilterRow>
							<div>Creation date: </div>
							<span>{state.creationDate}</span>
						</FilterRow>
						<FilterRow>
							<div>Real hero: </div>
							<input
								disabled={!isEdit}
								type="checkbox"
								checked={state.realHero}
								onChange={handleBoolHumanChange('realHero')}
							/>
						</FilterRow>
						<FilterRow>
							<div>Has toothpick: </div>
							<input
								disabled={!isEdit}
								type="checkbox"
								checked={state.hasToothpick}
								onChange={handleBoolHumanChange('hasToothpick')}
							/>
						</FilterRow>
						<FilterRow>
							<div>Impact speed: </div>
							<RowTextInput
								disabled={!isEdit}
								value={state.impactSpeed}
								onChange={handleTextHumanChange('impactSpeed')}
							/>
						</FilterRow>
						<FilterRow>
							<label>weaponType</label>
							<select
								disabled={!isEdit}
								value={state.weaponType}
								onChange={handleTextHumanChange('weaponType')}>
								{ALL_WEAPONS.map(weapon => (
									<option key={weapon} value={weapon}>
										{weapon.toLocaleLowerCase()}
									</option>
								))}
							</select>
						</FilterRow>
						<FilterRow>
							<div>Soundtrack name: </div>
							<RowTextInput
								disabled={!isEdit}
								value={state.soundtrackName}
								onChange={handleTextHumanChange('soundtrackName')}
							/>
						</FilterRow>
						<FilterRow>
							<div>Minutes of waiting: </div>
							<RowTextInput
								disabled={!isEdit}
								value={state.minutesOfWaiting}
								onChange={handleTextHumanChange('minutesOfWaiting')}
							/>
						</FilterRow>
						<FilterRow>
							<div>Car name: </div>
							<RowTextInput
								disabled={!isEdit}
								value={state.car.name}
								onChange={handleTextCarChange('name')}
							/>
						</FilterRow>
						<FilterRow>
							<div>Car cool: </div>
							<input
								disabled={!isEdit}
								type="checkbox"
								checked={state.car.cool}
								onChange={handleBoolCarChange('cool')}
							/>
						</FilterRow>
						{isEdit ? (
							<>
								<button onClick={handleSave}>save</button>
								<button onClick={handleCancel}>cancel</button>
							</>
						) : (
							<button onClick={handleEdit}>edit</button>
						)}
					</>
				)),
			)}
		</div>
	);
});
