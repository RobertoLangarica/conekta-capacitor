declare module '@capacitor/core' {
  interface PluginRegistry {
    ConektaPlugin: ConektaPluginPlugin;
  }
}

export interface ConektaPluginPlugin {
  setPublicKey(key: string): Promise<void>;
  getPublicKey(): Promise<{ key: string }>;
  setLanguage(language: string): Promise<void>;
  getLanguage(): Promise<{ language: string }>;
  createToken(card: { number: string, name: string, cvc: string, exp_month: string, exp_year: string }): Promise<any>;
  setApiVersion(api_version: string): Promise<void>;
  getApiVersion(): Promise<{ api_version: string }>;
  deviceFingerPrint(): Promise<{ fingerprint: string }>
  getBaseUri(): Promise<{ uri: string }>;
}
