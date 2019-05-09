import { action, computed, observable } from 'mobx';

class CommonStore {
  @observable language = 'en';

  @action
  setLanguage(language) {
    this.language = language;

    localStorage.setItem('language', language);
  }

  @computed
  get getLanguage() {
    return this.language;
  }
}

export default new CommonStore();
