import { createApplication } from '@angular/platform-browser';
import { createCustomElement } from '@angular/elements';
import { appConfig } from './app/app.config';
import { App } from './app/app';

(async () => {
  const app = await createApplication(appConfig);
  const element = createCustomElement(App, { injector: app.injector });
  customElements.define('solidblocks-contact', element);
})();
