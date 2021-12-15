import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IActivePairs, defaultValue } from 'app/shared/model/active-pairs.model';

export const ACTION_TYPES = {
  FETCH_ACTIVEPAIRS_LIST: 'activePairs/FETCH_ACTIVEPAIRS_LIST',
  FETCH_ACTIVEPAIRS: 'activePairs/FETCH_ACTIVEPAIRS',
  CREATE_ACTIVEPAIRS: 'activePairs/CREATE_ACTIVEPAIRS',
  UPDATE_ACTIVEPAIRS: 'activePairs/UPDATE_ACTIVEPAIRS',
  DELETE_ACTIVEPAIRS: 'activePairs/DELETE_ACTIVEPAIRS',
  RESET: 'activePairs/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IActivePairs>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type ActivePairsState = Readonly<typeof initialState>;

// Reducer

export default (state: ActivePairsState = initialState, action): ActivePairsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ACTIVEPAIRS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ACTIVEPAIRS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ACTIVEPAIRS):
    case REQUEST(ACTION_TYPES.UPDATE_ACTIVEPAIRS):
    case REQUEST(ACTION_TYPES.DELETE_ACTIVEPAIRS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_ACTIVEPAIRS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ACTIVEPAIRS):
    case FAILURE(ACTION_TYPES.CREATE_ACTIVEPAIRS):
    case FAILURE(ACTION_TYPES.UPDATE_ACTIVEPAIRS):
    case FAILURE(ACTION_TYPES.DELETE_ACTIVEPAIRS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACTIVEPAIRS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACTIVEPAIRS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ACTIVEPAIRS):
    case SUCCESS(ACTION_TYPES.UPDATE_ACTIVEPAIRS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ACTIVEPAIRS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/active-pairs';

// Actions

export const getEntities: ICrudGetAllAction<IActivePairs> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_ACTIVEPAIRS_LIST,
  payload: axios.get<IActivePairs>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IActivePairs> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ACTIVEPAIRS,
    payload: axios.get<IActivePairs>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IActivePairs> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ACTIVEPAIRS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IActivePairs> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ACTIVEPAIRS,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IActivePairs> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ACTIVEPAIRS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
