import { from, Observable } from 'rxjs';
import { Builder, parseStringPromise } from 'xml2js';
import { Either, left, right } from 'fp-ts/Either';

export interface Car {
	name?: string;
	cool: boolean;
}

export interface Coordinates {
	x: number;
	y: number;
}

export type WeaponType = 'HAMMER' | 'AXE' | 'SHOTGUN' | 'KNIFE';

export interface HumanBeing {
	id: number;
	name: string;
	coordinates: Coordinates;
	creationDate: string;
	hasToothpick: boolean;
	impactSpeed: number;
	soundtrackName: string;
	minutesOfWaiting: number;
	weaponType: WeaponType;
	car: Car;
}

export interface HumanBeingProvider {
	getAllHumans: () => Observable<Either<Error, HumanBeing[]>>;
	getHuman: (id: number) => Observable<Either<Error, HumanBeing>>;
	createHuman: (human: HumanBeing) => Observable<Either<Error, number>>;
	updateHuman: (human: HumanBeing) => Observable<Either<Error, void>>;
	deleteHuman: (id: number) => Observable<Either<Error, void>>;
	// var-specific methods
	deleteAnyMinutesOfWaitingEqual: (minutesOfWaiting: number) => Observable<Either<Error, void>>;
	countAllSoundtrackNameLess: (soundtrackName: string) => Observable<Either<Error, number>>;
	findAllMinutesOfWaitingLess: (minutesOfWaiting: number) => Observable<Either<Error, HumanBeing[]>>;
}

const humanBeingAPI = 'human-being';

export const createHumanBeingProvider = (): HumanBeingProvider => {
	const xmlBuilder = new Builder();

	const requestAPI = (init: RequestInit, url: string = ''): Observable<Either<Error, any>> =>
		from(
			fetch(`${humanBeingAPI}/${url}`, init)
				.then(res => {
					if (res.status === 200) {
						return res.text();
					}
					throw new Error(`${res.status}: ${res.statusText}`);
				})
				.then(r => parseStringPromise(r, { explicitArray: false }))
				.then(data => right(data))
				.catch(e => left<Error>(e)),
		);

	const getAllHumans = (): Observable<Either<Error, HumanBeing[]>> =>
		requestAPI({
			method: 'GET',
		});

	const getHuman = (id: number): Observable<Either<Error, HumanBeing>> =>
		requestAPI(
			{
				method: 'GET',
			},
			`${id}`,
		);

	const createHuman = (human: HumanBeing): Observable<Either<Error, number>> =>
		requestAPI({
			method: 'POST',
			body: xmlBuilder.buildObject(human),
		});

	const updateHuman = (human: HumanBeing): Observable<Either<Error, void>> =>
		requestAPI({
			method: 'PUT',
			body: xmlBuilder.buildObject(human),
		});

	const deleteHuman = (id: number): Observable<Either<Error, void>> =>
		requestAPI(
			{
				method: 'DELETE',
			},
			`${id}`,
		);

	const deleteAnyMinutesOfWaitingEqual = (minutesOfWaiting: number): Observable<Either<Error, void>> =>
		requestAPI(
			{
				method: 'DELETE',
			},
			`minutesOfWaiting=${minutesOfWaiting}`,
		);

	const countAllSoundtrackNameLess = (soundtrackName: string): Observable<Either<Error, number>> =>
		requestAPI(
			{
				method: 'POST',
			},
			`soundtrackNameLess=${soundtrackName}`,
		);

	const findAllMinutesOfWaitingLess = (minutesOfWaiting: number): Observable<Either<Error, HumanBeing[]>> =>
		requestAPI(
			{
				method: 'GET',
			},
			`minutesOfWaitingLess=${minutesOfWaiting}`,
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
