export interface IActivePairs {
  id?: string;
  exchangeId?: string;
  email?: string;
}

export const defaultValue: Readonly<IActivePairs> = {
  id: '',
  exchangeId: '',
  email: '',
};
