// Polyfill for Node.js global variables in browser environment
if (typeof global === 'undefined') {
  window.global = globalThis;
}