import commonjs from '@rollup/plugin-commonjs';
import resolve from '@rollup/plugin-node-resolve';
import css from 'rollup-plugin-css-porter';
import { terser } from "rollup-plugin-terser";

export default [{
  input: 'js/js_src/highlight/highlight.js',
  output: {
    file: 'js/lib/highlight.pack.js',
    format: 'iife',
    name: 'hljs',
    plugins: [
      terser()
    ]
  },
  plugins: [
    commonjs(),
    resolve(),
    css({
      raw: false,
      minified: 'css/highlight.min.css',
    })
  ]
}, {
  input: 'js/js_src/highlight/main.js',
  output: {
    file: 'js/lib/highlight-bundle.js',
    format: 'iife',
    plugins: [
      terser()
    ]
  }
}, {
  input: 'js/js_src/highlight/worker.js',
  output: {
    file: 'js/lib/highlight-worker.js',
    format: 'iife',
    plugins: [
      terser()
    ]
  }
}]
