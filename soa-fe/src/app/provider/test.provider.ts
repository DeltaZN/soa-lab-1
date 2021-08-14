import { BASE_URL } from '../App';
import { from, Observable } from 'rxjs';
import { Either, left, right } from 'fp-ts/Either';

export interface TestProvider {
	readonly doTest: () => Observable<Either<any, string>>;
}

export const createTestProvider = (): TestProvider => {
	return {
		doTest: () =>
			from(
				fetch(`${BASE_URL}/hello-servlet`, {
					method: 'POST',
				})
					.then(res => res.text())
					.then(data => right(data))
					.catch(e => left<any>(e)),
			),
	};
};
