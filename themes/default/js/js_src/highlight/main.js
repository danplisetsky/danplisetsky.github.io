function loadListener() {
  const preCodes = document.querySelectorAll('pre code');
  let mapPreCodesText = new Map();
  let mapPreCodes = new Map();
  let i = 0;
  preCodes.forEach(pc => {
    mapPreCodesText.set(i, {langs: Array.from(pc.classList), text: pc.textContent });
    mapPreCodes.set(i, pc);
    i = i + 1;
  });

  const worker = new Worker('/js/lib/highlight-worker.js');
  worker.onmessage = event => {
    const result = event && event.data;
    const preCodeIndex = result && result.index;
    const preCodeValue = result && result.value;

    const preCode = mapPreCodes.get(preCodeIndex);

    if (preCode) {
      preCode.innerHTML = preCodeValue;
      preCode.classList.add('hljs');
    }
  };

  worker.postMessage(mapPreCodesText);
}

function loadScript(url) {
  return new Promise((resolve, reject) => {
    let script = document.createElement('script');
    script.type = 'application/javascript';
    script.src = url;

    script.onload = ev => resolve();
    script.onerror = ev => reject(Error('failed to load script: ' + url));

    document.head.appendChild(script);
  });

}

function highlightBlocks(_event) {
  document.querySelectorAll('pre code').forEach(block => {
    hljs.highlightBlock(block);
  });
}

if (window.Worker) {
  window.onload = loadListener;
} else {
  loadScript('/js/lib/highlight.pack.js')
    .then(() => {
      if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', highlightBlocks);
      } else {
        highlightBlocks();
      }
    })
    .catch(err => console.log(err));
}
