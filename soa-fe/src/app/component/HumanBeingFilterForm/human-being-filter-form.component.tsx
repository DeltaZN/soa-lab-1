import React, { ChangeEvent, memo, useCallback, useState } from 'react';
import { WeaponType } from '../../provider/human-being.provider';

export interface HumanBeingFormProps {
	readonly onFilterChange: (filter: string) => void;
}

interface FilterForm {
	name?: string;
	coordinatesX?: number;
	coordinatesY?: number;
	creationDate?: string;
	realHero?: boolean;
	hasToothpick?: boolean;
	impactSpeed?: number;
	soundtrackName?: string;
	minutesOfWaiting?: number;
	weaponType?: WeaponType;
	carName?: string;
	carCool?: boolean;
}

type Config<Type> = {
	[Property in keyof Type]: boolean;
};

type FormStateEnabled = Config<FilterForm>;

export const HumanBeingFilterForm = memo<HumanBeingFormProps>(props => {
	const { onFilterChange } = props;

	const [formState, setFormState] = useState<FormStateEnabled>({
		name: false,
		coordinatesX: false,
		coordinatesY: false,
		creationDate: false,
		realHero: false,
		hasToothpick: false,
		impactSpeed: false,
		soundtrackName: false,
		minutesOfWaiting: false,
		weaponType: false,
		carName: false,
		carCool: false,
	});
	const [filterForm, setFilterForm] = useState<FilterForm>({});

	const updateFilter = useCallback(
		(newValue: FilterForm, newFormState: FormStateEnabled) => {
			const keys = Object.keys(newValue);
			const filterStr =
				(keys.length ? '?' : '') +
				keys
					// @ts-ignore
					.map(key => (newFormState[key] ? `${key}=${newValue[key]}` : ''))
					.filter(s => s.length)
					.join('&');
			onFilterChange(filterStr);
		},
		[formState],
	);

	const onFormEnabledChange = useCallback(
		(key: keyof FormStateEnabled) => () => {
			const newFormState = { ...formState, [key]: !formState[key] };
			setFormState({ ...formState, [key]: !formState[key] });
			updateFilter({ ...filterForm }, newFormState);
		},
		[formState],
	);

	const onFormChange = useCallback(
		(key: keyof FilterForm) => (e: ChangeEvent<HTMLInputElement>) => {
			const newValue = { ...filterForm, [key]: e.target.value };
			setFilterForm(newValue);
			updateFilter(newValue, formState);
		},
		[formState, filterForm],
	);

	return (
		<div>
			<div>
				<input type="checkbox" checked={formState.name} onChange={onFormEnabledChange('name')} />
				<label>name</label>
				<input disabled={!formState.name} value={filterForm.name} onChange={onFormChange('name')} />
			</div>
			<div>
				<input
					type="checkbox"
					checked={formState.coordinatesX}
					onChange={onFormEnabledChange('coordinatesX')}
				/>
				<label>coordinatesX</label>
				<input
					disabled={!formState.coordinatesX}
					value={filterForm.coordinatesX}
					onChange={onFormChange('coordinatesX')}
				/>
			</div>
			<div>
				<input
					type="checkbox"
					checked={formState.coordinatesY}
					onChange={onFormEnabledChange('coordinatesY')}
				/>
				<label>coordinatesY</label>
				<input
					disabled={!formState.coordinatesY}
					value={filterForm.coordinatesY}
					onChange={onFormChange('coordinatesY')}
				/>
			</div>
			<div>
				<input type="checkbox" checked={formState.realHero} onChange={onFormEnabledChange('realHero')} />
				<label>realHero</label>
				<input disabled={!formState.realHero} type="checkbox" checked={filterForm.realHero} />
			</div>
			<div>
				<input
					type="checkbox"
					checked={formState.hasToothpick}
					onChange={onFormEnabledChange('hasToothpick')}
				/>
				<label>hasToothpick</label>
				<input disabled={!formState.hasToothpick} type="checkbox" checked={filterForm.hasToothpick} />
			</div>
			<div>
				<input type="checkbox" checked={formState.impactSpeed} onChange={onFormEnabledChange('impactSpeed')} />
				<label>impactSpeed</label>
				<input
					disabled={!formState.impactSpeed}
					value={filterForm.impactSpeed}
					onChange={onFormChange('impactSpeed')}
				/>
			</div>
			<div>
				<input
					type="checkbox"
					checked={formState.soundtrackName}
					onChange={onFormEnabledChange('soundtrackName')}
				/>
				<label>soundtrackName</label>
				<input
					disabled={!formState.soundtrackName}
					value={filterForm.soundtrackName}
					onChange={onFormChange('soundtrackName')}
				/>
			</div>
			<div>
				<input
					type="checkbox"
					checked={formState.minutesOfWaiting}
					onChange={onFormEnabledChange('minutesOfWaiting')}
				/>
				<label>minutesOfWaiting</label>
				<input
					disabled={!formState.minutesOfWaiting}
					value={filterForm.minutesOfWaiting}
					onChange={onFormChange('minutesOfWaiting')}
				/>
			</div>
			<div>
				<input type="checkbox" checked={formState.weaponType} onChange={onFormEnabledChange('weaponType')} />
				<label>weaponType</label>
				<input disabled={!formState.weaponType} value={filterForm.weaponType} />
			</div>
			<div>
				<input type="checkbox" checked={formState.carName} onChange={onFormEnabledChange('carName')} />
				<label>car name</label>
				<input disabled={!formState.carName} value={filterForm.carName} onChange={onFormChange('carName')} />
			</div>
			<div>
				<input type="checkbox" checked={formState.carCool} onChange={onFormEnabledChange('carCool')} />
				<label>car cool</label>
				<input disabled={!formState.carCool} checked={filterForm.carCool} />
			</div>
		</div>
	);
});
