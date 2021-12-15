import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './active-pairs.reducer';
import { IActivePairs } from 'app/shared/model/active-pairs.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IActivePairsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ActivePairsDetail = (props: IActivePairsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { activePairsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          ActivePairs [<b>{activePairsEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="exchangeId">Exchange Id</span>
          </dt>
          <dd>{activePairsEntity.exchangeId}</dd>
          <dt>
            <span id="email">Email</span>
          </dt>
          <dd>{activePairsEntity.email}</dd>
        </dl>
        <Button tag={Link} to="/active-pairs" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/active-pairs/${activePairsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ activePairs }: IRootState) => ({
  activePairsEntity: activePairs.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ActivePairsDetail);
