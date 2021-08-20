const path = require('path');

module.exports = {
	extends: ['./node_modules/@devexperts/lint/.eslintrc.json'],
	parser: '@typescript-eslint/parser',
	parserOptions: {
		project: path.resolve(__dirname, 'tsconfig.eslint.json'),
		extraFileExtensions: ['.json'],
	},
	plugins: ['eslint-plugin-node'],
	rules: {
		'no-new-func': 'error',
		"@typescript-eslint/class-name-casing": 'off',
		'@typescript-eslint/ban-types': [
			'error',
			{
				types: {
					Function: 'Use () => void instead',
				},
			},
		],
		'@typescript-eslint/consistent-type-definitions': ['error', 'interface'],
		'no-restricted-syntax': [
			// casts are NOT ALLOWED!
			'error',
			"TSAsExpression[typeAnnotation.typeName.name!='const']",
		],
	}
};
