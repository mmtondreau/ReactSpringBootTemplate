import { useEffect } from 'react';
import { useAppSelector, useAppDispatch } from '../../app/hooks';
import { RootState } from '../../app/store';

import { decrement, increment, getCount } from './counter-slice';

export default function Counter() {
  // The `state` arg is correctly typed as `RootState` already
  const count = useAppSelector((state: RootState) => state.counter.value);
  const dispatch = useAppDispatch();
  useEffect(() => {
    dispatch(getCount());
  });
  return (
    <div>
      <div>
        <button type="button" aria-label="Increment value" onClick={() => dispatch(increment())}>
          Increment
        </button>
        <span>count is: {count}</span>
        <button type="button" aria-label="Decrement value" onClick={() => dispatch(decrement())}>
          Decrement
        </button>
      </div>
    </div>
  );
}
