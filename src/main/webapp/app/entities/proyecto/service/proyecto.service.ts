import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProyecto, NewProyecto } from '../proyecto.model';

export type PartialUpdateProyecto = Partial<IProyecto> & Pick<IProyecto, 'id'>;

export type EntityResponseType = HttpResponse<IProyecto>;
export type EntityArrayResponseType = HttpResponse<IProyecto[]>;

@Injectable({ providedIn: 'root' })
export class ProyectoService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/proyectos');

  create(proyecto: NewProyecto): Observable<EntityResponseType> {
    return this.http.post<IProyecto>(this.resourceUrl, proyecto, { observe: 'response' });
  }

  update(proyecto: IProyecto): Observable<EntityResponseType> {
    return this.http.put<IProyecto>(`${this.resourceUrl}/${this.getProyectoIdentifier(proyecto)}`, proyecto, { observe: 'response' });
  }

  partialUpdate(proyecto: PartialUpdateProyecto): Observable<EntityResponseType> {
    return this.http.patch<IProyecto>(`${this.resourceUrl}/${this.getProyectoIdentifier(proyecto)}`, proyecto, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProyecto>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProyecto[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProyectoIdentifier(proyecto: Pick<IProyecto, 'id'>): number {
    return proyecto.id;
  }

  compareProyecto(o1: Pick<IProyecto, 'id'> | null, o2: Pick<IProyecto, 'id'> | null): boolean {
    return o1 && o2 ? this.getProyectoIdentifier(o1) === this.getProyectoIdentifier(o2) : o1 === o2;
  }

  addProyectoToCollectionIfMissing<Type extends Pick<IProyecto, 'id'>>(
    proyectoCollection: Type[],
    ...proyectosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const proyectos: Type[] = proyectosToCheck.filter(isPresent);
    if (proyectos.length > 0) {
      const proyectoCollectionIdentifiers = proyectoCollection.map(proyectoItem => this.getProyectoIdentifier(proyectoItem));
      const proyectosToAdd = proyectos.filter(proyectoItem => {
        const proyectoIdentifier = this.getProyectoIdentifier(proyectoItem);
        if (proyectoCollectionIdentifiers.includes(proyectoIdentifier)) {
          return false;
        }
        proyectoCollectionIdentifiers.push(proyectoIdentifier);
        return true;
      });
      return [...proyectosToAdd, ...proyectoCollection];
    }
    return proyectoCollection;
  }
}
export class ProyectoStateService {}
