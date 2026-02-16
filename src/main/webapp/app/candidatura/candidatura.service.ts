import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { CandidaturaDTO } from 'app/candidatura/candidatura.model';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class CandidaturaService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/candidaturas';

  getAllCandidaturas() {
    return this.http.get<CandidaturaDTO[]>(this.resourcePath);
  }

  getCandidatura(id: number) {
    return this.http.get<CandidaturaDTO>(this.resourcePath + '/' + id);
  }

  createCandidatura(candidaturaDTO: CandidaturaDTO) {
    return this.http.post<number>(this.resourcePath, candidaturaDTO);
  }

  updateCandidatura(id: number, candidaturaDTO: CandidaturaDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, candidaturaDTO);
  }

  deleteCandidatura(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

  getUsuarioValues() {
    return this.http.get<Record<string, string>>(this.resourcePath + '/usuarioValues')
        .pipe(map(transformRecordToMap));
  }

  getProyectoValues() {
    return this.http.get<Record<string, string>>(this.resourcePath + '/proyectoValues')
        .pipe(map(transformRecordToMap));
  }

}
