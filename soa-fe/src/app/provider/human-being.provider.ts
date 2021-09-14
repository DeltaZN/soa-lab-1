import { from, map, Observable } from 'rxjs';
import { Builder, parseStringPromise } from 'xml2js';
import { Either, left, right } from 'fp-ts/Either';
import { pipe } from 'fp-ts/function';
import { array, either, option } from 'fp-ts';

export interface Car {
	name?: string;
	cool: boolean;
}

export interface Coordinates {
	x: number;
	y: number;
}

export const ALL_WEAPONS = ['HAMMER', 'AXE', 'SHOTGUN', 'KNIFE'] as const;

export type WeaponType = typeof ALL_WEAPONS[number];

export interface HumanBeing {
	id: number;
	name: string;
	coordinates: Coordinates;
	creationDate: string;
	realHero: boolean;
	hasToothpick: boolean;
	impactSpeed: number;
	soundtrackName: string;
	minutesOfWaiting: number;
	weaponType: WeaponType;
	car: Car;
}

export interface PaginationResult {
	readonly totalPages: number;
	readonly currentPage: number;
	readonly totalItems: number;
	readonly humans: HumanBeing[];
}

export type PCoordinates = Partial<Coordinates>;
export type PCar = Partial<Car>;
export type PHumanBeing = Partial<HumanBeing> & { car: PCar; coordinates: PCoordinates };

export interface HumanBeingProvider {
	getAllHumans: (params?: string) => Observable<Either<ServerResponse<any>, PaginationResult>>;
	getHuman: (id: number) => Observable<Either<ServerResponse<any>, HumanBeing>>;
	createHuman: (human: HumanBeing) => Observable<Either<ServerResponse<any>, number>>;
	updateHuman: (human: HumanBeing) => Observable<Either<ServerResponse<any>, void>>;
	deleteHuman: (id: number) => Observable<Either<ServerResponse<any>, void>>;
	// var-specific methods
	deleteAnyMinutesOfWaitingEqual: (minutesOfWaiting: number) => Observable<Either<ServerResponse<any>, number>>;
	countAllSoundtrackNameLess: (soundtrackName: string) => Observable<Either<ServerResponse<any>, number>>;
	findAllMinutesOfWaitingLess: (minutesOfWaiting: number) => Observable<Either<ServerResponse<any>, HumanBeing[]>>;
}

export interface ServerResponse<T> {
	readonly body: T;
}

const humanBeingAPI = `human-being`;

export const createHumanBeingProvider = (): HumanBeingProvider => {
	const xmlBuilder = new Builder();

	const requestAPI = (init: RequestInit, url: string = ''): Observable<Either<ServerResponse<any>, any>> =>
		from(
			fetch(`${humanBeingAPI}${url}`, init)
				.then(res => {
					if (res.status === 200) {
						return res.text();
					}
					return res
						.text()
						.then(text => parseStringPromise(text, { explicitArray: false, ignoreAttrs: true }))
						.then(text => {
							throw text;
						});
				})
				.then(r => parseStringPromise(r, { explicitArray: false, ignoreAttrs: true }))
				.then(data => right(data))
				.catch(e => left<ServerResponse<any>>(e.server_response)),
		);

	const getAllHumans = (params?: string): Observable<Either<ServerResponse<any>, PaginationResult>> =>
		requestAPI(
			{
				method: 'GET',
			},
			params,
		).pipe(
			map(e =>
				pipe(
					e,
					either.map(d => ({
						totalPages: parseInt(d.pagination_result.totalPages, 10),
						currentPage: parseInt(d.pagination_result.currentPage, 10),
						totalItems: parseInt(d.pagination_result.totalItems, 10),
						humans: pipe(
							option.fromNullable(d.pagination_result.humans),
							option.chain(data => option.fromNullable(data.human)),
							option.map(data => (Array.isArray(data) ? data : [data])),
							option.getOrElse<HumanBeing[]>(() => []),
							array.map(human => ({
								...human,
								// @ts-ignore
								hasToothpick: human.hasToothpick === 'true',
								// @ts-ignore
								realHero: human.realHero === 'true',
								car: {
									...human.car,
									// @ts-ignore
									cool: human.car.cool === 'true',
								},
							})),
						),
					})),
				),
			),
		);

	const getHuman = (id: number): Observable<Either<ServerResponse<any>, HumanBeing>> =>
		requestAPI(
			{
				method: 'GET',
			},
			`/${id}`,
		).pipe(
			map(e =>
				pipe(
					e,
					either.map(d =>
						pipe(
							option.fromNullable(d.human_being),
							option.map(human => ({
								...human,
								// @ts-ignore
								hasToothpick: human.hasToothpick === 'true',
								// @ts-ignore
								realHero: human.realHero === 'true',
								car: {
									...human.car,
									// @ts-ignore
									cool: human.car.cool === 'true',
								},
							})),
							option.getOrElse(() => emptyHuman),
						),
					),
				),
			),
		);

	const createHuman = (human: HumanBeing): Observable<Either<ServerResponse<any>, number>> =>
		requestAPI({
			method: 'POST',
			body: xmlBuilder.buildObject({ human_being: human }),
		}).pipe(
			map(e =>
				pipe(
					e,
					either.map(d =>
						pipe(
							option.fromNullable(d.server_response),
							option.map(response => response.body),
							option.getOrElse<number>(() => 0),
						),
					),
				),
			),
		);

	const updateHuman = (human: HumanBeing): Observable<Either<ServerResponse<any>, void>> =>
		requestAPI({
			method: 'PUT',
			body: xmlBuilder.buildObject({ human_being: human }),
		});

	const deleteHuman = (id: number): Observable<Either<ServerResponse<any>, void>> =>
		requestAPI(
			{
				method: 'DELETE',
			},
			`/${id}`,
		);

	const deleteAnyMinutesOfWaitingEqual = (
		minutesOfWaiting: number,
	): Observable<Either<ServerResponse<any>, number>> =>
		requestAPI(
			{
				method: 'DELETE',
			},
			`?minutesOfWaiting=${minutesOfWaiting}`,
		).pipe(
			map(e =>
				pipe(
					e,
					either.map(d =>
						pipe(
							option.fromNullable(d.server_response),
							option.map(response => response.body),
							option.getOrElse<number>(() => 0),
						),
					),
				),
			),
		);

	const countAllSoundtrackNameLess = (soundtrackName: string): Observable<Either<ServerResponse<any>, number>> =>
		requestAPI(
			{
				method: 'POST',
			},
			`/count?soundtrackNameLess=${soundtrackName}`,
		).pipe(
			map(e =>
				pipe(
					e,
					either.map(d =>
						pipe(
							option.fromNullable(d.server_response),
							option.map(response => response.body),
							option.getOrElse<number>(() => 0),
						),
					),
				),
			),
		);

	const findAllMinutesOfWaitingLess = (
		minutesOfWaiting: number,
	): Observable<Either<ServerResponse<any>, HumanBeing[]>> =>
		requestAPI(
			{
				method: 'GET',
			},
			`?minutesOfWaitingLess=${minutesOfWaiting}`,
		).pipe(
			map(e =>
				pipe(
					e,
					either.map(d =>
						pipe(
							option.fromNullable(d.humans),
							option.chain(data => option.fromNullable(data.item)),
							option.map(data => (Array.isArray(data) ? data : [data])),
							option.getOrElse<HumanBeing[]>(() => []),
							array.map(human => ({
								...human,
								// @ts-ignore
								hasToothpick: human.hasToothpick === 'true',
								// @ts-ignore
								realHero: human.realHero === 'true',
								car: {
									...human.car,
									// @ts-ignore
									cool: human.car.cool === 'true',
								},
							})),
						),
					),
				),
			),
		);

	return {
		getAllHumans,
		getHuman,
		createHuman,
		updateHuman,
		deleteHuman,
		deleteAnyMinutesOfWaitingEqual,
		countAllSoundtrackNameLess,
		findAllMinutesOfWaitingLess,
	};
};

export const emptyHuman: HumanBeing = {
	id: 0,
	name: '',
	coordinates: { x: 0, y: 0 },
	creationDate: '',
	hasToothpick: false,
	realHero: false,
	impactSpeed: 0,
	soundtrackName: '',
	minutesOfWaiting: 0,
	weaponType: 'AXE',
	car: {
		name: '',
		cool: false,
	},
};
