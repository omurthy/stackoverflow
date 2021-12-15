import { Moment } from 'moment';

export interface IEmployee {
  id?: string;
  firstName?: string;
  lastName?: string;
  email?: string;
  phoneNumber?: string;
  hireDate?: string;
  salary?: number;
  commissionPct?: number;
}

export const defaultValue: Readonly<IEmployee> = {};
