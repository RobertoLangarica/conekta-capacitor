<template>
  <q-page class="flex flex-center">
    <div class="row items-center q-col-gutter-x-md" style="width:100%; height:100%;">
      <!-- KEY -->
      <div class="col-12 row items-center q-col-gutter-md q-mt-xl">
        <q-input class="col-12 col-md-10" v-model="key" filled label="Enter your Conekta public key"/>
        <div class="col-12 col-md-auto row">
        <q-btn class="col-12" label="set" color="primary" @click="onKeySet"/>
        </div>

        <q-input class="col-12 col-md-10" v-model="c_key" readonly outlined label="Conekta public key"/>
        <div class="col-12 col-md-auto row">
        <q-btn class="col-12" label="get" color="secondary" @click="onKeyGet"/>
        </div>
      </div>

      <!-- LANGUAGE -->
      <div class="col-12 row items-center q-pt-xl q-col-gutter-md">
        <q-input class="col-12 col-md-10" v-model="lang" filled label="Language"/>
        <div class="col-12 col-md-auto row">
        <q-btn class="col-12" label="set" color="primary" @click="onLangSet"/>
        </div>

        <q-input class="col-12 col-md-10" v-model="c_lang" readonly outlined label="Conekta language"/>
        <div class="col-12 col-md-auto row">
        <q-btn class="col-12" label="get" color="secondary" @click="onLangGet"/>
        </div>
      </div>

      <!-- API VERSION -->
      <div class="col-12 row items-center q-pt-xl q-col-gutter-md">
        <q-input class="col-12 col-md-10" v-model="api" filled label="Api version"/>
        <div class="col-12 col-md-auto row">
        <q-btn class="col-12" label="set" color="primary" @click="onApiSet"/>
        </div>

        <q-input class="col-12 col-md-10" v-model="c_api" readonly outlined label="Conekta api version"/>
        <div class="col-12 col-md-auto row">
        <q-btn class="col-12" label="get" color="secondary" @click="onApiGet"/>
        </div>
      </div>

      <!-- DEVICE FINGERPRINT -->
      <div class="col-12 row items-center q-pt-xl q-col-gutter-md">
        <q-input class="col-12 col-md-10" v-model="c_fingerprint" readonly outlined label="Get device Fingerprint"/>
        <div class="col-12 col-md-auto row">
        <q-btn class="col-12" label="get" color="secondary" @click="onFingerprintGet"/>
        </div>
      </div>

      <!-- BASE URI -->
      <div class="col-12 row items-center q-pt-xl q-col-gutter-md">
        <q-input class="col-12 col-md-10" v-model="c_uri" readonly outlined label="Get base URI"/>
        <div class="col-12 col-md-auto row">
        <q-btn class="col-12" label="get" color="secondary" @click="onBaseUriGet"/>
        </div>
      </div>

      <!-- CREATE TOKEN -->
      <div class="col-12 row items-center q-py-xl q-col-gutter-md">
         <!-- <q-field class="col-12" label="Card data"> -->
           <!-- <template v-slot:control> -->
             <div class="col-12 row q-mt-md q-col-gutter-md" >
               <span>CARD DATA</span>
              <q-input class="col-12" filled v-model="c_number" outlined placeholder="NUMBER" hint="4242-42424242-4242"/>
              <q-input class="col-4"  filled v-model="c_cvc" outlined placeholder="CVC" hint="123"/>
              <q-input class="col-4"  filled v-model="c_month" outlined placeholder="MONTH" hint="02"/>
              <q-input class="col-4"  filled v-model="c_year" outlined placeholder="YEAR" hint="2020"/>
              <q-input class="col-12" filled v-model="c_name" outlined placeholder="NAME"/>
              <div class="col-12 row">
              <q-btn class="col-12" label="get token" color="secondary" @click="onTokenGet"/>
              </div>
              <q-input class="col-12" v-model="c_token" readonly outlined label="Conekta token"/>
            </div>
           <!-- </template> -->
        <!-- </q-field> -->

      </div>
    </div>
  </q-page>
</template>

<script lang="ts">
/* eslint-disable */
import { Plugins } from '@capacitor/core'
const { ConektaPlugin } = Plugins
import 'conekta-capacitor' // import needed for web support

export default {
  name: 'PageIndex',
  data () {
    return {
      key: process.env.PUBLIC_KEY || '',
      c_key: 'N/A',
      lang: 'en',
      c_lang: 'N/A',
      api: '',
      c_api: 'N/A',
      c_fingerprint: 'N/A',
      c_uri: 'N/A',
      c_number: process.env.CARD_NUMBER || '',
      c_name: 'John Doe card',
      c_month: process.env.CARD_EXP_MONTH || '',
      c_year: process.env.CARD_EXP_YEAR || '',
      c_cvc: process.env.CARD_CVC || '',
      c_token: '',
      Conekta: undefined
    }
  },
  methods: {
    notify(positive:boolean, message:string){
      this.$q.notify({
          type: positive ? 'positive':'negative',
          message: message
        })
    },
    onKeySet () {
      try {
        ConektaPlugin.setPublicKey({key:this.key})
        this.onKeyGet(false)
        this.notify(true,`setPublicKey successful`)
      } catch (e) {
        console.log(e)
        this.notify(false,e)
      }
    },
    async onKeyGet (notify = true) {
      try {
        const result = await ConektaPlugin.getPublicKey()
        console.log(result)
        if(notify){
          this.notify(true,`Conketa public key read successfully`)
        }
        this.c_key = result.key
      } catch (e) {
        console.log(e)
        this.notify(false,e)
      }
    },
    onLangSet () {
      try {
        ConektaPlugin.setLanguage({language:this.lang})
        this.onLangGet(false)
        this.notify(true,`setLanguage successful`)
      } catch (e) {
        console.log(e)
        this.notify(false,e)
      }
    },
    async onLangGet (notify=true) {
      try {
        const result = await ConektaPlugin.getLanguage()
        console.log(result)
        if(notify){
          this.notify(true,`Conketa language read successfully`)
        }
        this.c_lang = result.language
      } catch (e) {
        this.notify(false,e.message || 'Error!')
        console.log(e)
      }
    },
    onApiSet () {
      try {
        ConektaPlugin.setApiVersion({api_version:this.api})
        this.onApiGet(false)
        this.notify(true,`setApiVersion successful`)
      } catch (e) {
        this.notify(false,e)
        console.log(e)
      }
    },
    async onApiGet (notify=true) {
      try {
        let result = await ConektaPlugin.getApiVersion()
        console.log(result)
        if(notify){
          this.notify(true,`Conketa Api version read successfully`)
        }
        this.c_api = result.api_version
      } catch (e) {
        this.notify(false,e.message || 'Error!')
        console.log(e)
      }
    },
    async onFingerprintGet () {
      try {
        let result = await ConektaPlugin.deviceFingerPrint()
        console.log(result)
        this.notify(true,`Conketa device fingerprint read successfully`)
        this.c_fingerprint = result.fingerprint
      } catch (e) {
        this.notify(false,e)
        console.log(e)
      }
    },
    async onBaseUriGet () {
      try {
        let result = await ConektaPlugin.getBaseUri()
        console.log(result)
        this.notify(true,`Conketa base URI read successfully`)
        this.c_uri = result.uri
      } catch (e) {
        this.notify(false,e)
        console.log(e)
      }
    },
    async onTokenGet () {
      let card = {
        number: this.c_number.toString(),
        name: this.c_name,
        cvc: this.c_cvc.toString(),
        exp_month: this.c_month.toString(),
        exp_year: this.c_year.toString(),
      }

      try {
        let result = await ConektaPlugin.createToken(card)
        /* Success schema
        {
          id:'',
          livemode:boolean,
          object: 'token',
          used: boolean
        }
        */
        console.log(result)
        if(result.object === "error"){
          this.notify(false,`Error creating the token: ${result.message}`)
        } else {
          this.notify(true,`Token created successfully`)
          this.c_token = result.id
        }
      } catch (e) {
        /* Error schema
        {
          code:String
          message:String,
          message_to_purchaser:String,
          object:"error",
          type:String,
          validation_error:String
        } */
        this.notify(false,`Error creating the token: ${e.message || 'Error!'}`)
        console.log(e)
      }
    }
  }
}
</script>
