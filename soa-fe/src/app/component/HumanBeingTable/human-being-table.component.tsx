import React, { ChangeEvent, memo, useCallback, useState } from 'react';
import { Car, HumanBeing } from '../../provider/human-being.provider';
import { RowTextInput } from './human-being-table.styled';

export interface HumanBeingTableProps {
	readonly humans: HumanBeing[];
	readonly onSave: (val: HumanBeing) => void;
}

export const HumanBeingTable = memo<HumanBeingTableProps>(props => {
	const { humans, onSave } = props;
	return humans.length ? (
		<table>
			<thead>
				<tr>
					<th>id</th>
					<th>name</th>
					<th>x</th>
					<th>y</th>
					<th>creation date</th>
					<th>real hero</th>
					<th>has toothpick</th>
					<th>impact speed</th>
					<th>soundtrack name</th>
					<th>minutes of waiting</th>
					<th>car name</th>
					<th>car cool</th>
					<th>control</th>
				</tr>
			</thead>
			<tbody>
				{humans.map(human => (
					<TableRow onSave={onSave} human={human} key={human.id} />
				))}
			</tbody>
		</table>
	) : (
		<div>no data</div>
	);
});

interface HumanBeingRowProps {
	readonly human: HumanBeing;
	readonly onSave: (val: HumanBeing) => void;
}

type KeysOfType<T, KT> = {
	[K in keyof T]: T[K] extends KT ? K : never;
}[keyof T];

const TableRow = memo<HumanBeingRowProps>(props => {
	const { human, onSave } = props;
	const [state, setState] = useState<HumanBeing>(human);
	const [isEdit, setIsEdit] = useState<boolean>(false);

	const handleSave = useCallback(() => {
		setIsEdit(false);
		onSave(state);
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
			setState({ ...state, [key]: e.target.value }),
		[state],
	);

	const handleCoordsChange = useCallback(
		(key: 'x' | 'y') => (e: ChangeEvent<HTMLInputElement | HTMLSelectElement>) =>
			setState({ ...state, coordinates: { ...state.coordinates, [key]: e.target.value } }),
		[state],
	);

	const handleBoolHumanChange = useCallback(
		(key: KeysOfType<HumanBeing, boolean>) => () => setState({ ...state, [key]: !state[key] }),
		[state],
	);

	const handleTextCarChange = useCallback(
		(key: keyof Car) => (e: ChangeEvent<HTMLInputElement | HTMLSelectElement>) =>
			setState({ ...state, car: { ...state.car, [key]: e.target.value } }),
		[state],
	);

	const handleBoolCarChange = useCallback(
		(key: keyof Car) => () => setState({ ...state, car: { ...state.car, [key]: !state.car[key] } }),
		[state],
	);

	return (
		<tr>
			<td>{human.id}</td>
			<td>
				{isEdit ? (
					<RowTextInput value={state.name} onChange={handleTextHumanChange('name')} />
				) : (
					<div>{state.name}</div>
				)}
			</td>
			<td>
				{isEdit ? (
					<RowTextInput value={state.coordinates.x} onChange={handleCoordsChange('x')} />
				) : (
					<div>{state.coordinates.x}</div>
				)}
			</td>
			<td>
				{isEdit ? (
					<RowTextInput value={state.coordinates.y} onChange={handleCoordsChange('y')} />
				) : (
					<div>{state.coordinates.y}</div>
				)}
			</td>
			<td>
				{isEdit ? (
					<RowTextInput value={state.creationDate} onChange={handleTextHumanChange('creationDate')} />
				) : (
					<div>{state.creationDate}</div>
				)}
			</td>
			<td>
				<input
					disabled={!isEdit}
					type="checkbox"
					checked={state.realHero}
					onChange={handleBoolHumanChange('realHero')}
				/>
			</td>
			<td>
				<input
					disabled={!isEdit}
					type="checkbox"
					checked={state.hasToothpick}
					onChange={handleBoolHumanChange('hasToothpick')}
				/>
			</td>
			<td>
				{isEdit ? (
					<RowTextInput value={state.impactSpeed} onChange={handleTextHumanChange('impactSpeed')} />
				) : (
					<div>{state.impactSpeed}</div>
				)}
			</td>
			<td>
				{isEdit ? (
					<RowTextInput value={state.soundtrackName} onChange={handleTextHumanChange('soundtrackName')} />
				) : (
					<div>{state.soundtrackName}</div>
				)}
			</td>
			<td>
				{isEdit ? (
					<RowTextInput value={state.minutesOfWaiting} onChange={handleTextHumanChange('minutesOfWaiting')} />
				) : (
					<div>{state.minutesOfWaiting}</div>
				)}
			</td>
			<td>
				{isEdit ? (
					<RowTextInput value={state.car.name} onChange={handleTextCarChange('name')} />
				) : (
					<div>{state.car.name}</div>
				)}
			</td>
			<td>
				<input
					disabled={!isEdit}
					type="checkbox"
					checked={state.car.cool}
					onChange={handleBoolCarChange('cool')}
				/>
			</td>
			<td>
				{isEdit ? (
					<>
						<button onClick={handleSave}>save</button>
						<button onClick={handleCancel}>cancel</button>
					</>
				) : (
					<button onClick={handleEdit}>edit</button>
				)}
			</td>
		</tr>
	);
});
