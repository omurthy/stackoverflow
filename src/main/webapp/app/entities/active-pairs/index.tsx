import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ActivePairs from './active-pairs';
import ActivePairsDetail from './active-pairs-detail';
import ActivePairsUpdate from './active-pairs-update';
import ActivePairsDeleteDialog from './active-pairs-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ActivePairsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ActivePairsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ActivePairsDetail} />
      <ErrorBoundaryRoute path={match.url} component={ActivePairs} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ActivePairsDeleteDialog} />
  </>
);

export default Routes;
