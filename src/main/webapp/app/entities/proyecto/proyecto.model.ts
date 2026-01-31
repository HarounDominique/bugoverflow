import { IUser } from 'app/entities/user/user.model';
import { tipocategoria } from 'app/entities/enumerations/tipocategoria.model';
import { tipoestado } from 'app/entities/enumerations/tipoestado.model';

export interface IProyecto {
  id: number;
  titulo?: string | null;
  descripcion?: string | null;
  urlrepo?: string | null;
  categoria?: keyof typeof tipocategoria | null;
  estado?: keyof typeof tipoestado | null;
  autor?: Pick<IUser, 'id' | 'login'> | null;
  colaboradores?: Pick<IUser, 'id' | 'login'>[] | null;
}

export type NewProyecto = Omit<IProyecto, 'id'> & { id: null };
