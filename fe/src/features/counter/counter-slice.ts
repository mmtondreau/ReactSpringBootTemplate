import { gql } from '@apollo/client';
import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import client from '../../servies/apollo-client';
import { AppDispatch } from '../../app/store';
// Define a type for the slice state
interface CounterState {
  value: number;
  error?: string;
}

// Define the initial state using that type
const initialState: CounterState = {
  value: 0,
  error: undefined,
};

export const counterSlice = createSlice({
  name: 'counter',
  // `createSlice` will infer the state type from the `initialState` argument
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = undefined;
    },
    setError: (state, action: PayloadAction<string>) => {
      state.error = action.payload;
    },
    setCount: (state, action: PayloadAction<number>) => {
      state.value = action.payload;
    },
    increment: (state) => {
      state.value += 1;
    },
    decrement: (state) => {
      state.value -= 1;
    },
    // Use the PayloadAction type to declare the contents of `action.payload`
    incrementByAmount: (state, action: PayloadAction<number>) => {
      state.value += action.payload;
    },
  },
});

export const { setCount, clearError, setError } = counterSlice.actions;

export default counterSlice.reducer;

export const getCount = () => {
  return (dispatch: AppDispatch) => {
    dispatch(clearError());
    client
      .query({
        query: gql`
          query getCount {
            count
          }
        `,
        fetchPolicy: 'network-only',
      })
      .then((response) => dispatch(setCount(response.data.count)))
      .catch((e) => dispatch(setError(e.message)));
  };
};

export const increment = () => {
  return (dispatch: AppDispatch) => {
    dispatch(clearError());
    client
      .mutate({
        mutation: gql`
          mutation incrementCount {
            incrementCount
          }
        `,
      })
      .then((response) => dispatch(setCount(response.data.incrementCount)))
      .catch((e) => dispatch(setError(e.message)));
  };
};

export const decrement = () => {
  return (dispatch: AppDispatch) => {
    dispatch(clearError());
    client
      .mutate({
        mutation: gql`
          mutation decrementCount {
            decrementCount
          }
        `,
      })
      .then((response) => dispatch(setCount(response.data.decrementCount)))
      .catch((e) => dispatch(setError(e.message)));
  };
};
