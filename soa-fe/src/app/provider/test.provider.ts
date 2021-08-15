import { from, Observable } from 'rxjs';
import { Either, left, right } from 'fp-ts/Either';
import { parseStringPromise } from 'xml2js';

export interface TestProvider {
	readonly doTest: () => Observable<Either<any, string>>;
}

export const createTestProvider = (): TestProvider => {
	return {
		doTest: () =>
			from(
				fetch(`hello-servlet`, {
					method: 'POST',
				})
					.then(res => res.text())
					.then(r => parseStringPromise(r, { explicitArray: false }))
					.then(data => right(data))
					.catch(e => left<any>(e)),
			),
	};
};
