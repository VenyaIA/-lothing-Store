export interface Customer {
  id: number;
  email: string;
  firstname: string;
  lastname: string;
  username: string;
  phoneNumber: string;
  roles?: string[];
}
