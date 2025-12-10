const { app, BrowserWindow, dialog } = require('electron')
const path = require('path')
const { spawn } = require('child_process')
const http = require('http')
const net = require('net')
const fs = require('fs')

let backendProc = null

function resolveBackendPaths() {
  const isDev = !app.isPackaged || process.env.ELECTRON_DEV === '1'
  const jarName = 'warehouse-management-1.0.0.jar'
  const javaExe = process.platform === 'win32' ? 'java.exe' : 'java'
  const paths = {}
  if (isDev) {
    paths.jar = path.resolve(__dirname, '../../backend/target/', jarName)
    paths.java = path.resolve(__dirname, '../../backend/target/runtime/bin/', javaExe)
  } else {
    paths.jar = path.join(process.resourcesPath, 'backend', jarName)
    paths.java = path.join(process.resourcesPath, 'backend', 'runtime', 'bin', javaExe)
  }
  return paths
}

function findAvailablePort(start = 8080, max = 8100) {
  return new Promise((resolve, reject) => {
    const tryPort = (p) => {
      if (p > max) return reject(new Error('No free port'))
      const server = net.createServer()
      server.once('error', () => {
        tryPort(p + 1)
      })
      server.once('listening', () => {
        server.close(() => resolve(p))
      })
      server.listen(p, '127.0.0.1')
    }
    tryPort(start)
  })
}

function startBackend(port) {
  const p = resolveBackendPaths()
  let javaPath = p.java
  const useBundledJava = fs.existsSync(javaPath)
  if (!useBundledJava) {
    javaPath = 'java'
  }
  const userHome = process.env.USERPROFILE || process.env.HOME || app.getPath('home')
  const logDir = path.join(userHome, '.warehouse')
  try { fs.mkdirSync(logDir, { recursive: true }) } catch {}
  const logFile = path.join(logDir, 'backend.log')
  const out = fs.createWriteStream(logFile, { flags: 'a' })
  backendProc = spawn(javaPath, ['-Dserver.address=127.0.0.1', '-Dserver.port=' + String(port), '-jar', p.jar], { stdio: ['ignore', 'pipe', 'pipe'] })
  if (backendProc.stdout) backendProc.stdout.pipe(out)
  if (backendProc.stderr) backendProc.stderr.pipe(out)
}

function waitForBackendReady(port, timeoutMs = 30000) {
  const deadline = Date.now() + timeoutMs
  return new Promise((resolve, reject) => {
    const attempt = () => {
      http.get(`http://127.0.0.1:${port}/api/health`, (res) => {
        if (res.statusCode === 200) {
          resolve()
        } else {
          if (Date.now() > deadline) reject(new Error('Backend not ready'))
          else setTimeout(attempt, 500)
        }
      }).on('error', () => {
        if (Date.now() > deadline) reject(new Error('Backend not ready'))
        else setTimeout(attempt, 500)
      })
    }
    attempt()
  })
}

function createWindow(port) {
  const win = new BrowserWindow({
    width: 1200,
    height: 800,
    webPreferences: {
      preload: path.join(__dirname, 'preload.js')
    }
  })
  win.setTitle(`Warehouse Management (${port})`)
  win.loadURL(`http://127.0.0.1:${port}`)
}

app.on('ready', async () => {
  try {
    const port = await findAvailablePort(8080, 8200)
    startBackend(port)
    try {
      await waitForBackendReady(port)
    } catch (e) {}
    createWindow(port)
  } catch (e) {
    dialog.showErrorBox('Startup error', e.message)
    app.quit()
  }
})

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') app.quit()
})

app.on('before-quit', () => {
  if (backendProc && !backendProc.killed) {
    try { backendProc.kill() } catch {}
  }
})
