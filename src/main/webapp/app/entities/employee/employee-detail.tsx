import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './employee.reducer';
import { IEmployee } from 'app/shared/model/employee.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IEmployeeDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const EmployeeDetail = (props: IEmployeeDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { employeeEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          Employee [<b>{employeeEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="firstName">First Name</span>
          </dt>
          <dd>{employeeEntity.firstName}</dd>
          <dt>
            <span id="lastName">Last Name</span>
          </dt>
          <dd>{employeeEntity.lastName}</dd>
          <dt>
            <span id="email">Email</span>
          </dt>
          <dd>{employeeEntity.email}</dd>
          <dt>
            <span id="phoneNumber">Phone Number</span>
          </dt>
          <dd>{employeeEntity.phoneNumber}</dd>
          <dt>
            <span id="hireDate">Hire Date</span>
          </dt>
          <dd>{employeeEntity.hireDate ? <TextFormat value={employeeEntity.hireDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="salary">Salary</span>
          </dt>
          <dd>{employeeEntity.salary}</dd>
          <dt>
            <span id="commissionPct">Commission Pct</span>
          </dt>
          <dd>{employeeEntity.commissionPct}</dd>
        </dl>
        <Button tag={Link} to="/employee" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/employee/${employeeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ employee }: IRootState) => ({
  employeeEntity: employee.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(EmployeeDetail);
