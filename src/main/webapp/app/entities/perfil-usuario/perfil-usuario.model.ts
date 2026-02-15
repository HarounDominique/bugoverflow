import { IUser } from 'app/entities/user/user.model';

export interface IPerfilUsuario {
  id: number;
  nombreVisible?: string | null;
  bio?: string | null;
  github?: string | null;
  webPersonal?: string | null;
  avatarUrl?: string | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewPerfilUsuario = Omit<IPerfilUsuario, 'id'> & { id: null };
