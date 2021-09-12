import React, { memo, useCallback, useEffect, useState } from 'react';
import { HumanBeing, PaginationResult } from '../../provider/human-being.provider';
import { PageN } from './pagination.styled';
import { HumanBeingTable } from '../HumanBeingTable/human-being-table.component';

export interface PaginationSettings {
	readonly currentPage: number;
	readonly pageSize: number;
}

export interface PaginationProps {
	readonly paginationResult: PaginationResult;
	readonly updateHuman: (human: HumanBeing) => void;
	readonly updatePagination: (val: string) => void;
}

const getPageNumeration = (pagination: PaginationResult, selectPage: (page: number) => void) => {
	const elems: JSX.Element[] = [];
	for (let i = 1; i <= pagination.totalPages; i++) {
		elems.push(
			<PageN key={i} selected={pagination.currentPage === i} onClick={() => selectPage(i)}>
				{i}
			</PageN>,
		);
	}
	return elems;
};

const defaultPagination = {
	currentPage: 1,
	pageSize: 5,
};

export const Pagination = memo<PaginationProps>(props => {
	const { paginationResult, updateHuman, updatePagination } = props;

	const [pagination, setPagination] = useState<PaginationSettings>(defaultPagination);

	const selectPage = useCallback(
		(page: number) => {
			setPagination({ ...pagination, currentPage: page });
		},
		[pagination],
	);

	useEffect(() => {
		updatePagination(`pageIdx=${pagination.currentPage}&pageSize=${pagination.pageSize}`);
	}, [pagination]);

	return (
		<div>
			<HumanBeingTable humans={paginationResult.humans} onSave={updateHuman} />
			<div>
				{getPageNumeration(paginationResult, selectPage)}
				<PageSizeSelector />
			</div>
		</div>
	);
});

interface PageSizeSelectorProps {}

const PageSizeSelector = memo<PageSizeSelectorProps>(props => {
	return <div />;
});
