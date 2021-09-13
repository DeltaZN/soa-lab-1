import React, { ChangeEvent, memo, useCallback, useContext, useState } from 'react';
import { none, Option, some } from 'fp-ts/Option';
import { Either } from 'fp-ts/Either';
import { constNull, constVoid, pipe } from 'fp-ts/function';
import { either, option } from 'fp-ts';
import { context } from '../../App';
import { HumanBeing } from '../../provider/human-being.provider';
import { HumanBeingCreationForm } from '../HumanBeingCreationForm/human-being-creation-form.component';
import { HumanBeingTable } from '../HumanBeingTable/human-being-table.component';

export interface SpecialPageProps {}

const randomHuman: HumanBeing = {
	id: 0,
	name: 'test',
	coordinates: { x: 1, y: 2 },
	creationDate: '',
	hasToothpick: true,
	realHero: false,
	impactSpeed: 1,
	soundtrackName: 'smells like...',
	minutesOfWaiting: 32,
	weaponType: 'SHOTGUN',
	car: {
		name: 'wolkswagen',
		cool: false,
	},
};

export const SpecialPage = memo<SpecialPageProps>(_ => {
	const [id, setId] = useState('');
	const [minutesOfWaiting, setMinutesOfWaiting] = useState('');
	const [soundtrackName, setSoundtrackName] = useState('');
	const [label, setLabel] = useState<Option<string>>(none);
	const [human, setHuman] = useState<Option<HumanBeing>>(none);
	const [humans, setHumans] = useState<Option<HumanBeing[]>>(none);

	const { humanBeingProvider } = useContext(context);

	const handleInput = useCallback((e: ChangeEvent<HTMLInputElement>) => {
		setId(e.target.value);
	}, []);

	const handleInputMinutes = useCallback((e: ChangeEvent<HTMLInputElement>) => {
		setMinutesOfWaiting(e.target.value);
	}, []);

	const handleInputSoundtrack = useCallback((e: ChangeEvent<HTMLInputElement>) => {
		setSoundtrackName(e.target.value);
	}, []);

	const handleResult = (res: Either<Error, any>) =>
		pipe(
			res,
			either.fold(
				e => setLabel(some(e.toString())),
				data => setLabel(some(JSON.stringify(data))),
			),
		);

	const onClickCreateHandler = useCallback(() => {
		humanBeingProvider.createHuman(randomHuman).subscribe(res =>
			pipe(
				res,
				either.fold(
					e => {
						setLabel(some(e.toString()));
						setHuman(none);
					},
					id => {
						setLabel(some(`Successful created id - ${id}`));
					},
				),
			),
		);
	}, [humanBeingProvider]);

	const onClickGetByIdHandler = useCallback(() => {
		humanBeingProvider.getHuman(parseInt(id, 10)).subscribe(res =>
			pipe(
				res,
				either.fold(
					e => {
						setLabel(some(e.toString()));
						setHuman(none);
					},
					data => {
						setLabel(none);
						setHuman(some(data));
					},
				),
			),
		);
	}, [humanBeingProvider, id]);

	const onClickDeleteByIdHandler = useCallback(() => {
		humanBeingProvider.deleteHuman(parseInt(id, 10)).subscribe(res =>
			pipe(
				res,
				either.fold(
					e => {
						setLabel(some(e.toString()));
						setHuman(none);
					},
					_ => {
						setLabel(some('Delete successful'));
						setHuman(none);
						setHumans(none);
					},
				),
			),
		);
	}, [humanBeingProvider, id]);

	const onClickCountSoundtrackNameLess = useCallback(() => {
		humanBeingProvider.countAllSoundtrackNameLess(soundtrackName).subscribe(handleResult);
	}, [humanBeingProvider, soundtrackName]);

	const onClickFindMinutesOfWaitingLess = useCallback(() => {
		humanBeingProvider.findAllMinutesOfWaitingLess(parseInt(minutesOfWaiting, 10)).subscribe(res =>
			pipe(
				res,
				either.fold(
					e => {
						setLabel(some(e.toString()));
						setHuman(none);
					},
					data => {
						setLabel(none);
						setHumans(some(data));
					},
				),
			),
		);
	}, [humanBeingProvider, minutesOfWaiting]);

	const onClickDeleteMinutesOfWaitingEqual = useCallback(() => {
		humanBeingProvider.deleteAnyMinutesOfWaitingEqual(parseInt(minutesOfWaiting, 10)).subscribe(res =>
			pipe(
				res,
				either.fold(
					e => {
						setLabel(some(e.toString()));
						setHuman(none);
					},
					data => {
						setLabel(some(`Deleted an item with id ${data}`));
						setHuman(none);
						setHumans(none);
					},
				),
			),
		);
	}, [humanBeingProvider, minutesOfWaiting]);

	const updateHandle = useCallback(
		(human: HumanBeing) => {
			humanBeingProvider.updateHuman(human).subscribe(res =>
				pipe(
					res,
					either.fold(
						e => {
							setLabel(some(e.toString()));
							setHuman(none);
						},
						_ => setHuman(some({ ...human })),
					),
				),
			);
		},
		[humanBeingProvider],
	);

	return (
		<div>
			<div>
				<label>ID</label>
				<input onChange={handleInput} value={id} />
				<button onClick={onClickGetByIdHandler}>Find by id!</button>
				<button onClick={onClickDeleteByIdHandler}>DeleteId!</button>
			</div>
			<div>
				<label>Minutes of waiting</label>
				<input onChange={handleInputMinutes} value={minutesOfWaiting} />
				<button onClick={onClickFindMinutesOfWaitingLess}>FindAllMinutesOfWaitingLess!</button>
				<button onClick={onClickDeleteMinutesOfWaitingEqual}>DeleteAnyMinutesOfWaitingEqual!</button>
			</div>
			<div>
				<label>Soundtrack name less</label>
				<input onChange={handleInputSoundtrack} value={soundtrackName} />
				<button onClick={onClickCountSoundtrackNameLess}>Count SoundtrackName Less!</button>
			</div>
			<button onClick={onClickCreateHandler}>TestCreate!</button>
			<div>
				{pipe(
					label,
					option.getOrElse(() => ''),
				)}
			</div>
			<HumanBeingCreationForm human={human} onSave={updateHandle} />
			{pipe(
				humans,
				option.fold(constNull, humans => (
					<HumanBeingTable humans={humans} onSave={constVoid} editable={false} />
				)),
			)}
		</div>
	);
});
