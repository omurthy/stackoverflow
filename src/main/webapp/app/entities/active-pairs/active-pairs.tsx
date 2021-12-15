import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction, ICrudSearchAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './active-pairs.reducer';
import { IActivePairs } from 'app/shared/model/active-pairs.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IActivePairsProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> { }

export const ActivePairs = (props: IActivePairsProps) => {


  useEffect(() => {
    props.getEntities();
  }, []);
  const { activePairsList, match, loading = true } = props;
  const myDataSource = props.activePairsList;

  const [myActivePairsList, setActivePairsList] = useState(props.activePairsList);
  const [dropdownList, setDropdownList] = useState([])
  const [focus, setFocus] = useState(false)

  const [searchValue, setSearchValue] = useState('')
  const [arrOfSelectedExId, setArrOfSelectedExId] = useState([])

  useEffect(() => {
    const unrepeated = activePairsList.reduce((acc, curr) => {
      if (acc.find(item => item.exchangeId === curr.exchangeId)) {
        return acc
      }
      return acc.concat(curr)
    }, [])

    const regexes = [`${searchValue}`];
    const filtered = unrepeated.filter(item => regexes.some(regex => item.exchangeId.match(regex)));
    setDropdownList(filtered);
    setActivePairsList(activePairsList);
  }, [searchValue, activePairsList])

  useEffect(() => {

    if (arrOfSelectedExId.length) {
      setActivePairsList(myDataSource.filter(activePairs => {
        return arrOfSelectedExId.includes(activePairs.exchangeId)
      }))
    } else {
      setActivePairsList(myDataSource)
    }
  }, [arrOfSelectedExId])



  return (
    <div>
      <input value={searchValue} onFocus={() => setFocus(true)} onBlur={(e) => setFocus(false)} onChange={e => setSearchValue(e.target.value)} />
      {(dropdownList.length !== 0) && focus && (<div className="dropdown-parent">
        {
          dropdownList.map((item, i) => {
            return <div key={'drp' + i} className={arrOfSelectedExId.includes(item.exchangeId) ? 'selected' : ''} onMouseDown={(e) => {
              e.preventDefault()
              setArrOfSelectedExId(prev => {
                if (prev.includes(item.exchangeId)) {
                  return prev.filter(x => x !== item.exchangeId)
                } else {
                  return prev.concat(item.exchangeId)
                }
              })
              // setSearchValue(item.exchangeId)
            }}>{item.exchangeId}</div>
          })
        }
      </div>)}
      <h2 id="active-pairs-heading">
        Active Pairs
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Active Pairs
        </Link>
      </h2>
      <div className="table-responsive">
        {myActivePairsList && myActivePairsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Exchange Id</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {myActivePairsList.map((activePairs, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${activePairs.id}`} color="link" size="sm">
                      {activePairs.id}
                    </Button>
                  </td>
                  <td>{activePairs.exchangeId}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${activePairs.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${activePairs.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${activePairs.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Active Pairs found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ activePairs }: IRootState) => ({
  activePairsList: activePairs.entities,
  loading: activePairs.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ActivePairs);
