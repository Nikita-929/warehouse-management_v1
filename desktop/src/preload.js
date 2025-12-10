const { contextBridge } = require('electron')
contextBridge.exposeInMainWorld('env', { electron: true })
