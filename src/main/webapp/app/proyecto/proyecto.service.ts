import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { ProyectoDTO } from 'app/proyecto/proyecto.model';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class ProyectoService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/proyectos';

  getAllProyectoes() {
    return this.http.get<ProyectoDTO[]>(this.resourcePath);
  }

  getProyecto(id: number) {
    return this.http.get<ProyectoDTO>(this.resourcePath + '/' + id);
  }

  createProyecto(proyectoDTO: ProyectoDTO) {
    return this.http.post<number>(this.resourcePath, proyectoDTO);
  }

  updateProyecto(id: number, proyectoDTO: ProyectoDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, proyectoDTO);
  }

  deleteProyecto(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

  getAutorValues() {
    return this.http.get<Record<string, string>>(this.resourcePath + '/autorValues')
        .pipe(map(transformRecordToMap));
  }

  getSkillsValues() {
    return this.http.get<Record<string, string>>(this.resourcePath + '/skillsValues')
        .pipe(map(transformRecordToMap));
  }

}
