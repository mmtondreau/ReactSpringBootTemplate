import { fireEvent, render, screen } from '@testing-library/react';
import user from '@testing-library/user-event';
import { act } from 'react-dom/test-utils';
import { Provider } from 'react-redux';
import App from '../src/App';
import store from '../src/app/store';

jest.mock('../src/apollo-client.ts', () => {
  let count = 0;
  return {
    default: {
      query: jest.fn(() => Promise.resolve(count)),
      mutate: jest.fn(() => {
        count += 1;
        Promise.resolve(count);
      }),
    },
  };
});

// See https://reactjs.org/docs/testing-recipes.html
test('renders main page', async () => {
  render(
    <Provider store={store}>
      <App />
    </Provider>,
  );
  const increamentButton = await screen.findByText('Increment');
  expect(screen.getByText('count is: 0')).toBeInTheDocument();
  await act(() => user.click(increamentButton));
  expect(screen.getByText('count is: 1')).toBeInTheDocument();
});

test('snapshots match', async () => {
  const { getByText, asFragment } = render(
    <Provider store={store}>
      <App />
    </Provider>,
  );
  const firstRender = asFragment();
  fireEvent.click(getByText('Increment'));
  expect(firstRender).toMatchDiffSnapshot(asFragment());
});
