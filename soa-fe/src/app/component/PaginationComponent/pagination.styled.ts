import styled from 'styled-components';

export interface PageNProps {
	readonly selected: boolean;
}

export const PageN = styled.span<PageNProps>`
	margin-right: 5px;
	border-bottom: ${props => (props.selected ? '1px solid;' : 'none')};
	:hover {
		cursor: pointer;
	}
	user-select: none;
`;
