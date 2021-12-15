import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './employee.reducer';
import { IEmployee } from 'app/shared/model/employee.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IEmployeeUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const EmployeeUpdate = (props: IEmployeeUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { employeeEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/employee');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.hireDate = convertDateTimeToServer(values.hireDate);

    if (errors.length === 0) {
      const entity = {
        ...employeeEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="stackoverflowApp.employee.home.createOrEditLabel">Create or edit a Employee</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : employeeEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="employee-id">ID</Label>
                  <AvInput id="employee-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="firstNameLabel" for="employee-firstName">
                  First Name
                </Label>
                <AvField id="employee-firstName" type="text" name="firstName" />
              </AvGroup>
              <AvGroup>
                <Label id="lastNameLabel" for="employee-lastName">
                  Last Name
                </Label>
                <AvField id="employee-lastName" type="text" name="lastName" />
              </AvGroup>
              <AvGroup>
                <Label id="emailLabel" for="employee-email">
                  Email
                </Label>
                <AvField id="employee-email" type="text" name="email" />
              </AvGroup>
              <AvGroup>
                <Label id="phoneNumberLabel" for="employee-phoneNumber">
                  Phone Number
                </Label>
                <AvField id="employee-phoneNumber" type="text" name="phoneNumber" />
              </AvGroup>
              <AvGroup>
                <Label id="hireDateLabel" for="employee-hireDate">
                  Hire Date
                </Label>
                <AvInput
                  id="employee-hireDate"
                  type="datetime-local"
                  className="form-control"
                  name="hireDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.employeeEntity.hireDate)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="salaryLabel" for="employee-salary">
                  Salary
                </Label>
                <AvField id="employee-salary" type="string" className="form-control" name="salary" />
              </AvGroup>
              <AvGroup>
                <Label id="commissionPctLabel" for="employee-commissionPct">
                  Commission Pct
                </Label>
                <AvField id="employee-commissionPct" type="string" className="form-control" name="commissionPct" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/employee" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  employeeEntity: storeState.employee.entity,
  loading: storeState.employee.loading,
  updating: storeState.employee.updating,
  updateSuccess: storeState.employee.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(EmployeeUpdate);
