import React from 'react';
import '../App.css';
import { AppContainer } from './App.styled';
import { createHumanBeingProvider, HumanBeingProvider } from './provider/human-being.provider';
import { MainPage } from './component/MainPageComponent/main-page.component';
import { BrowserRouter, Link, Route, Switch } from 'react-router-dom';
import { SpecialPage } from './component/SpecialPageComponent/special-page.component';

export interface LabContext {
	readonly humanBeingProvider: HumanBeingProvider;
}

const defaultContext: LabContext = {
	humanBeingProvider: createHumanBeingProvider(),
};

export const BASE_URL = `soa_be_war_exploded`;

export const context = React.createContext(defaultContext);

export const App = () => {
	return (
		<AppContainer>
			<BrowserRouter>
				<div>
					<nav>
						<ul>
							<li>
								<Link to={`/${BASE_URL}/`}>Main Page(Table)</Link>
							</li>
							<li>
								<Link to={`/${BASE_URL}/special`}>Special operations</Link>
							</li>
						</ul>
					</nav>
					<Switch>
						<Route path={`/${BASE_URL}/special`}>
							<SpecialPage />
						</Route>
						<Route path={`/${BASE_URL}/`}>
							<MainPage />
						</Route>
					</Switch>
				</div>
			</BrowserRouter>
		</AppContainer>
	);
};
