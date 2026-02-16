import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { SkillDTO } from 'app/skill/skill.model';


@Injectable({
  providedIn: 'root',
})
export class SkillService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/skills';

  getAllSkills() {
    return this.http.get<SkillDTO[]>(this.resourcePath);
  }

  getSkill(id: number) {
    return this.http.get<SkillDTO>(this.resourcePath + '/' + id);
  }

  createSkill(skillDTO: SkillDTO) {
    return this.http.post<number>(this.resourcePath, skillDTO);
  }

  updateSkill(id: number, skillDTO: SkillDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, skillDTO);
  }

  deleteSkill(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

}
