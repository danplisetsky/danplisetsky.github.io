function messageListener(event) {
  const mapPreCodes = event.data;
  mapPreCodes.forEach((val, key) => {
    const result = self.hljs.highlightAuto(val.text, val.langs);
    self.postMessage({ index: key, value: result.value });
  });
}

self.importScripts('/js/lib/highlight.pack.js');
self.onmessage = messageListener;
