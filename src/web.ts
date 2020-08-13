import { WebPlugin } from '@capacitor/core';
import { ConektaPluginPlugin } from './definitions';

export class ConektaPluginWeb extends WebPlugin implements ConektaPluginPlugin {

  Conekta: any;

  constructor() {
    super({
      name: 'ConektaPlugin',
      platforms: ['web'],
    });

    this.getSDK()
  }

  async getSDK() {
    if (this.Conekta) {
      return;
    }

    return new Promise((resolve, _reject) => {
      let url = 'https://cdn.conekta.io/js/latest/conekta.js'
      let script = document.createElement("script")
      script.type = "text/javascript"
      script.src = url
      script.async = true
      document.body.appendChild(script)
      script.addEventListener("load", () => {
        this.Conekta = (window as any).Conekta
        resolve()
      })
    })
  }

  async setPublicKey(params: { key: string }) {
    await this.getSDK()
    this.Conekta.setPublicKey(params.key)
  }

  async getPublicKey(): Promise<{ key: string }> {
    await this.getSDK()
    return { key: this.Conekta.getPublicKey() }
  }

  async setLanguage(params: { language: string }): Promise<void> {
    await this.getSDK()
    this.Conekta.setLanguage(params.language);
  }

  async getLanguage(): Promise<{ language: string }> {
    await this.getSDK()
    return { language: this.Conekta.getLanguage() }
  }

  async createToken(card: { number: string, name: string, cvc: string, exp_month: string, exp_year: string }): Promise<any> {
    return new Promise((resolve, reject) => {
      this.Conekta.Token.create({ card }, resolve, reject)
    })
  }

  setApiVersion(_params: { api_version: string }): Promise<void> {
    console.warn('setApiVersion is not available in Javascript SDK');
    return Promise.resolve()
  }

  getApiVersion(): Promise<{ api_version: string }> {
    console.warn('getApiVersion is not available in Javascript SDK');
    return Promise.reject('getApiVersion is not available in Javascript SDK')
  }

  deviceFingerPrint(): Promise<{ fingerprint: string }> {
    console.warn('deviceFingerPrint is not available in Javascript SDK');
    return Promise.reject('deviceFingerPrint is not available in Javascript SDK')
  }

  getBaseUri(): Promise<{ uri: string }> {
    console.warn('getBaseUri is not available in Javascript SDK');
    return Promise.reject('getBaseUri is not available in Javascript SDK')
  }
}

const ConektaPlugin = new ConektaPluginWeb();

export { ConektaPlugin };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(ConektaPlugin);
