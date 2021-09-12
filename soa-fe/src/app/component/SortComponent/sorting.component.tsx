import { ChangeEvent, memo, useCallback } from 'react';
import { FilterForm } from '../HumanBeingFilterForm/human-being-filter-form.component';
import { SortingContainer } from './sorting.styled';

type Property = keyof FilterForm | 'id';
type Label = string;

export type SortOption = [Property, Label];

export interface SortingProps {
	readonly options: SortOption[];
	readonly onChange: (val: string) => void;
}

export const Sorting = memo<SortingProps>(props => {
	const { options, onChange } = props;
	const handleChange = useCallback(
		(e: ChangeEvent<HTMLSelectElement>) => {
			const val = e.target.value;
			if (val === 'default') {
				onChange('');
			} else {
				onChange(`sort=${val}`);
			}
		},
		[onChange],
	);
	return (
		<SortingContainer>
			<select onChange={handleChange}>
				<option key={'default'} value={'default'}>
					default
				</option>
				{options.map(([property, label]) => (
					<option key={property} value={property}>
						{label}
					</option>
				))}
			</select>
		</SortingContainer>
	);
});
