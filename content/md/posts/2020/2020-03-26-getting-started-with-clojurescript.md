{:title "Getting Started with ClojureScript"
 :layout :post
 :tags ["ClojureScript", "Clojure", "programming"]
 :highlight-js true
 :music {:artist "Morrissey"
 	 :song "I Wish You Lonely"}
 :book {:author "Fyodor Dostoevsky"
        :title "The Brothers Karamazov"}}

TLDR: clone [this repo][clojurescript-template] to get started, or [this repo][cljs-example] for a more involved example (the latter is also live [here][cljs-example-live])

***

Clojure is famous for being amazing and a total bliss in every way... except when it comes to documentation. This is especially true of the [official ClojureScript][] website: the Quick Start guide doesn't even mention HTML, even though the idea is to compile ClojureScript to JavaScript and then reference it in an HTML file.

Let's see how we can build, step by step, a blissful ClojureScript development experience, and then we'll look at the benefits of using this weird, funny-looking, parentheses-heavy (there's a point behind them, I swear!) language.


### Intended Audience

Programmers familiar with (front end) development who'd like to code like it's 2020 -- not 1999. If being able to save the state of your app (and modify it on the fly) between hot reloads sounds good, read on.


### Step 0: Prerequisites

You'll need the following tools available on your PATH:

- [Node/npm][node-npm]
- [Clojure][]

If you're on a Mac, both are also available via [brew][].

Now we can go ahead and create a folder for our project:

```bash
mkdir clojurescript-template
```

### Step 1: HTML

We'll start with HTML. Create a folder called `public` with the following `index.html`:

```html
<body>
  <div id="app"></div>
  <script src="js/main.js"></script>
</body>
</html>
```

This should look familiar to React, etc. developers. We'll manipulate the DOM from our compiled main.js and we'll render the whole app into that one `div`.


### Step 2: Shadow CLJS and Deps

> [shadow-cljs][] provides everything you need to compile your ClojureScript code with a focus on simplicity and ease of use.

Pretty much.

Let's install it first:

```bash
echo '{}' > package.json # we'll only be using package.json for dependency management and scripts. Feel free to add other fields (name, author, etc.)
npm i -D shadow-cljs
```

And then initialize a config file:

```bash
./node_modules/.bin/shadow-cljs init
```

This will create a placeholder `shadow-cljs.edn` which looks like this:

```clojure
;; shadow-cljs configuration
{:source-paths
 ["src/dev"
  "src/main"
  "src/test"]

 :dependencies
 []

 :builds
 {}}
```

As you can see, shadow-cljs can manage dependencies and the source code, but we'll use the now official Clojure tool for that called [Deps][], mostly because at least one popular Clojure setup, IntelliJ + Cursive, does not yet support shadow-cljs.

So, let's create a `deps.edn` file and make it look like this:

```clojure
{:paths ["src/dev"]

 :deps  {thheller/shadow-cljs {:mvn/version "2.8.94"}}

 :aliases
 {:cljs
  {:extra-deps {}
   :extra-paths ["src/main" "src/test"]}}}
```

- `:paths` points to the code that only Deps needs to know about
- `:deps`  specifies the dependencies that, again, only Deps needs to know about

Under `:aliases` we'll have a map of, well, aliases. In this case, we want shadow-cljs to know about some `:extra-deps` (currently empty) and `:extra-paths`. We'll call that alias `:cljs`.

Now back to `shadow-cljs.edn`:
```clojure
{:deps
 {:aliases [:cljs]}

 :builds
 {}}
```

Here we tell it to use Deps (via the `:deps` key) to manage dependencies and source paths.


### Step 3: Hello, World

It's about time. Create the following folders: `src/main/foo`. Inside `foo`, create `core.cljs`:

```clojure
(ns foo.core)

(js/alert "Hello, World")

(println "Hello, World")
```

Now that we have some ClojureScript code, we'll need to compile it and place it under `public/js/main.js` (since that's where our `index.html` is pointing to).

Back to `shadow-cljs.edn`:

```clojure
{:deps
 {:aliases [:cljs]}

 :builds
 {:app {:target :browser
        :output-dir "public/js"
        :asset-path "js"
        :modules {:main {:entries [foo.core]}}}}}
```

See the `:app`? That's how we'll refer to this build configuration when issuing commands to `shadow-cljs` (more on that in a bit).

- [`:target`][target-browser]
> The :browser target produces output intended to run in a Browser environment. During development it supports live code reloading, REPL, CSS reloading. The release output will be minified by the Closure Compiler with :advanced optimizations.
- `:output-dir` is where our compiled output goes to
- `:asset-path` is the relative path from the root (where the main `index.html` is) to `:output-dir`
- `:modules` specify output js files. There can be many of them (just like in any other build tool we're used to: Webpack, Rollup, etc.). There's more to them than what we have in the configuration file right now, which is one module, `:main` (it'll become main.js) and the root namespace, `foo.core` (we defined that namespace previously in the `src/main/foo/core.cljs` file).

We're ready to go. Let's define helper scripts in `package.json` (feel free to skip this step and issue relevant commands directly):

```javascript
{
  "scripts": {
    "server": "shadow-cljs server",
    "app:compile": "shadow-cljs compile app",
    "app:release": "shadow-cljs release app"
  },
  "devDependencies": {
    "shadow-cljs": "^2.8.94"
  }
}
```

Our workflow is the following: have one shell session dedicated to running a shadow-cljs server (`npm run server`). That way, all the other shadow-cljs commands will use that server instance and take much less time to complete. There's even a dashboard you can navigate to:

`shadow-cljs - server version: 2.8.94 running at http://localhost:9630`

Now that we have our server running, try compiling the cljs code:

```bash
npm run app:compile
```

You'll see the output in `public/js`. Now start any static web server like 

```bash
python -m SimpleHTTPServer
```

or skip to the next section where we'll have `shadow-cljs` do that for us, and navigate to `index.html`. You should see a Hello, World popup and a message in the console.


### Step 4: All the Good Things

Now -- to the cool bits. We'll start slowly and build them up, step by step.

To start with, add this new script to `package.json`:

```javascript
{
  "scripts": {
    "server": "shadow-cljs server",
    "server:repl": "shadow-cljs clj-repl",
    "app:compile": "shadow-cljs compile app",
    "app:release": "shadow-cljs release app"
  },
  "devDependencies": {
    "shadow-cljs": "^2.8.94"
  }
}
```

Run it in a new terminal session:

```bash
npm run server:repl
```

You'll see a prompt this time around:

```clojure-repl
shadow-cljs - REPL - see (help)
To quit, type: :repl/quit
shadow.user=>
```

For now, let's treat this prompt like any other prompt from any other REPL (node, etc.). Type the following:

```clojure-repl
shadow.user=> (ns-aliases *ns*)
```
Which gives us
```clojure
{shadow "shadow.cljs.devtools.api"]}
```

That's helpful: we can type `shadow` instead of `shadow.cljs.devtools.api` to use functions from that namespace. What functions could those be? Let's try compiling our app again:

```clojure-repl
shadow.user=> (shadow/compile :app)
[:app] Compiling ...
[:app] Build completed. (43 files, 0 compiled, 0 warnings, 0.85s)
:done
```

That's neat: we can do everything from this REPL. Let's add two more scripts to `package.json` (at this point, more for reference's and completeness' sake than anything else):

```javascript
"app:watch": "shadow-cljs watch app",
"app:repl": "shadow-cljs cljs-repl app"
```

Back in our repl, let's try

```clojure-repl
shadow.user=> (shadow/watch :app)
[:app] Configuring build.
[:app] Compiling ...
[:app] Build completed. (135 files, 1 compiled, 0 warnings, 0.93s)
:watching
```

This is the same as running `npm run app:watch`, only, as we just saw with `(shadow/compile :app)`, we can do it from the REPL instead, which gives us a lot more control and flexibility (we'll talk more about it during [Coffee Break](/posts/2020-03-26-getting-started-with-clojurescript/#~~~_coffee_break_~~~)).

`watch` is a live-reload feature (again, nothing new so far, but we are going slowly after all). Let's briefly go back to `shadow-cljs.edn` file and add `:dev-http`:

```clojure
{:deps
 {:aliases [:cljs]}

 :dev-http {9000 "public"}
  
 :builds
 {:app {:target :browser
        :output-dir "public/js"
        :asset-path "js"
        :modules {:main {:entries [foo.core]}}}}}
```

That's the static server we talked about previously. Our running server will pick up the changes to the config automatically and display this:

`shadow-cljs - HTTP server available at http://localhost:9000`

Let's see the live-reload in action while we're at it. Open localhost:9000, change your `core.cljs` (maybe remove that annoying alert) and save it. You should see a logo in the left-hand corner, and your changes will be picked up and reloaded automatically.

Now to the `app:repl` script that we added. Go back to the server repl you still have prompting you for input and type:

```clojure-repl
shadow.user=> (shadow/repl :app)
cljs.user=>
```

The current namespace changed to `cljs.user`. Also, our REPL is connected to the browser now! Type

```clojure-repl
cljs.user=> (println 42)
42
nil
```

42 is printed in our REPL running in a terminal session. `nil` is the returned value of the `(println 42)` expression. But look at the console on localhost:9000 and you'll find that 42 made its way there as well.

Let's try something a bit more interesting:

```clojure-repl
=> (def hello-world (doto (.createElement js/document "p") (aset "innerHTML" "Hello, World")))
#'cljs.user/hello-world
=> (def app (.getElementById js/document "app"))
#'cljs.user/app
=> (.appendChild app hello-world)
```

Look in your browser -- now that's a proper __Hello, World__ example!


### Step 5: The State of the World

Now for the big finish. Let's add some state and UI to `core.cljs`:

```clojure
(ns foo.core)

;; STATE

(defonce state (atom 0))

;; HELPERS

(defn root []
  (.getElementById js/document "app"))

(defn counter []
  (.getElementById js/document "counter"))

(defn replaceChild [root new-child old-child]
  (.replaceChild root new-child old-child))

;; COMPONENTS

(defn new-counter []
  (doto (.createElement js/document "p")
    (.setAttribute "id" "counter")
    (aset "innerHTML" @state)))

(defn new-button-inc []
  (doto (.createElement js/document "button")
    (aset "innerHTML" "+1")
    (aset "onclick" (fn [_event]
                      (swap! state inc)
                      (replaceChild (root) (new-counter) (counter))))))

(defn new-button-reset []
  (doto (.createElement js/document "button")
    (aset "innerHTML" "reset")
    (aset "onclick" (fn [_event]
                      (reset! state 0)
                      (replaceChild (root) (new-counter) (counter))))))

;; RENDER

(defn render [root & components]
  (doseq [c components]
    (.append root c)))

(defn ^:export init
  []
  (aset (root) "innerHTML" "")
  (render (root) (new-counter) (new-button-inc) (new-button-reset)))
```

Save the file and issue the following commands in the REPL:

```clojure-repl
cljs.user=> (in-ns 'foo.core)
nil
foo.core=> (init)
nil
```

First, we're switching to the `foo.core` namespace (so we wouldn't have to prefix every command from that namespace, i.e., we could've instead typed `(foo.core/init)`)

Then, we call the `init` function, which renders our UI. Make sure that the buttons work, etc.

Now, once you're ready, click `reset` and type `@state` in the REPL. You should get 0. That's the current value of our `state`.

Now, type this:

```clojure-repl
foo.core=> (reset! state 42)
42
foo.core=> (init)
nil
```

That's interesting: we changed the state from our REPL, and now, re-rendering the UI, the changes are persisting.

Of course, this is still inefficient -- there's too much manual work.

Let's keep modifying `shadow-cljs.edn`:

```clojure
{:deps
 {:aliases [:cljs]}

 :dev-http {9000 "public"}

 :builds
 {:app {:target :browser
        :output-dir "public/js"
        :asset-path "js"
        :modules {:main {:entries [foo.core]
                         :init-fn foo.core/init}}
        :devtools {:repl-init-ns foo.core
                   :after-load foo.core/init}}}}
```

`:init-fn` specifies the function that will be called when the module is loaded initially (refresh `localhost:9000` to see it in action)

`:repl-init-ns` will switch the current namespace to `foo.core` when we connect to a running CLJS repl

`:after-load` specifies the function to run each time we're live-reloading

Now go back to the page and give `+1` a few clicks. Then modify some part of UI in `core.cljs`. For instance, replace `(aset "innerHTML" "+1")` with `(aset "innerHTML" "plus one")` and save the file. Did you notice that the UI has changed while the state stayed the same?

We're not stopping here, of course. Up until now, we've been using vanilla JavaScript from ClojureScript to render our UI. Now we're going to move to a de-facto standard in the ClojureScript world when it comes to framework-less UI development: **Reagent**.


### Step 6: Reagent

[Reagent][] bills itself as

> Minimalistic React for ClojureScript

Let's bring it in first via `deps.edn`:

```clojure
{:paths ["src/dev"]

 :deps  {thheller/shadow-cljs {:mvn/version "2.8.94"}}

 :aliases
 {:cljs
  {:extra-deps {reagent {:mvn/version "0.10.0"}}
   :extra-paths ["src/main" "src/test"]}}}
```

Every time we bring in an external dependency, we have to restart the server. So, kill your REPL and server and run `npm run server`. This will install Reagent (and React, too!).

Now let's go back to where we were. You can repeat the steps above or run the scripts we've defined (in two separate terminal sessions -- another reason to prefer doing everything from the REPL):

```bash
npm run app:watch
npm run app:repl
```

In `core.cljs`:

```clojure
(ns foo.core
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]))

;; STATE

(defonce state (r/atom 0))

;; COMPONENTS

(defn counter []
  [:p "Counter: " @state])

(defn button-inc []
  [:input {:type "button"
           :value "+1"
           :on-click (fn [_event]
                       (swap! state inc))}])

(defn button-reset []
  [:input {:type "button"
           :value "reset"
           :on-click (fn [_event]
                       (reset! state 0))}])

(defn container []
  [:div
   [counter]
   [button-inc]
   [button-reset]])

;; RENDER

(defn root []
  (.getElementById js/document "app"))

(defn ^:export init []
  (rdom/render [container] (root)))
```

Looks much cleaner, doesn't it? Save, refresh localhost:9000 and click around a bit.

From your REPL:

```clojure-repl
foo.core=> (reset! state 42)
```

This changes the state and you can see the update in the UI.

In `core.cljs`, replace

```clojure
:value "+1"
```

with

```clojure
:value "plus one"
```

and save. Neat, huh? The state stays with us, and is completely under our control, while we're free to change the UI in any way we want.


### ~~~ Coffee Break ~~~

Naturally, this is just the tip of the iceberg. We came nowhere near a real REPL-driven experience in this tutorial, but hopefully it gave you a taste of what's possible.

The eventual idea is to connect your editor to a running REPL and evaluate everything from the editor. Once you experience this kind of development, where feedback is immediate and control over state during runtime is as fine-grained as you want, it's really difficult to see the merits of the usual `change->save->compile->check->debug->change->etc.` cycle.

#### Parentheses

Let's quickly touch on them. Clojure is a LISP. Like any LISP, it has no syntax -- only data structures. List is one of them. Its literal representation is `()`. There are several ways to create one. Let's use the `list` function (we'll issue the following commands in the server REPL, so start one with `npm run server:repl`):

```clojure-repl
shadow.user=> (list 1 2)
(1 2)
shadow.user=> (type (list 1 2))
clojure.lang.PersistentList
```

The list we've created is [fully persistent][]; the implementation of `clojure.lang.PersistentList` (in Java) can be found [here][clojure.lang.PersistentList]. Note that a function invocation is also a list:

```clojure-repl
=> (+ 1 2)
3
=> (list + 1 2)
(#object[clojure.core$_PLUS_ 0x74a72a9d "clojure.core$_PLUS_@74a72a9d"] 1 2)
=> (eval (list + 1 2))
3
```

What if we tried (1 2)?

```clojure-repl
=> (1 2)
Execution error...
class java.lang.Long cannot be cast to class clojure.lang.IFn
```

So 1, which is `java.lang.Long`, does not implement `clojure.lang.IFn` -- we cannot invoke 1. What this gives us, the fact that function invocations are lists themselves, is that we end programming in data structures themselves -- for all intents and purposes, we program in Abstract Syntax Trees, which are inaccessible to us in many other languages. This gives us, users, the ability to extend the language which usually only the creators of a language have.

Let's look at two examples. We'll start with a simple one:

```clojure-repl
shadow.user=> (defmacro debug-plus-10 [expr]
  (let [new-expr (concat expr (list 10))]
    (println "debugging " new-expr)
    new-expr))
```

The ability to access the data structures is realized with macros. Here, we define one with `defmacro`. Clojure is an eagerly-evaluated language, but macros _don't_ evaluate their arguments. Instead, they return the data structure that will then be evaluated.

In this case, `debug-plus-10` expects an expression `expr`. On the next line, we concat 10 to that expression. Then we print the expression out and return it. Only then will the expression be evaluated.

Let's try it out:

```clojure-repl
=> (debug-plus-10 (+ 1 2))
debugging  (+ 1 2 10)
13
```

`(+ 1 2)` is the argument we pass to `debug-plus-10`. Since it's a list like any other, we can use all the rich sequence API on it. In this case, we use `concat` to append 10 to the end of the list. We print it out and return the resulting list `(+ 1 2 10)`, which then gets evaluated to 13.

And now for something slightly different. Consider C#'s [using][] statement:

```c#
using (var font1 = new Font("Arial", 10.0f))
{
    byte charset = font1.GdiCharSet;
}
```

`Font` implements `IDisposable`. `using` calls `Dispose` on the object correctly. But read [this][using-remarks]:

> You can achieve the same result by putting the object inside a try block and then calling Dispose in a finally block; __in fact, this is how the using statement is translated by the compiler__. The code example earlier expands to the following code at compile time (note the extra curly braces to create the limited scope for the object):
```c#
{
  var font1 = new Font("Arial", 10.0f);
  try
  {
    byte charset = font1.GdiCharSet;
  }
  finally
  {
    if (font1 != null)
      ((IDisposable)font1).Dispose();
  }
}
```

If there were no `using` in C#, you'd have to wait for the designers to put it in. In Clojure, we don't have to wait! Observe:

```clojure-repl
=> (defmacro using [resource & body]
  `(let [~(get resource 0) ~(get resource 1)]
     (try
      ~@body
      (finally
       (println "getting ready to close stream on file" ~(last (get resource 1)))
       (.close ~(get resource 0))))))
```

The backtick ` means, don't evaluate the following data structure _right now_, but return it to be evaluated at the call site. The tilde ~ means, _do_ evaluate the following data structure right now.

Let's try this macro out:

```clojure-repl
=> (using [fr (new java.io.FileReader "deps.edn")] (println "first char is:" (char (.read fr))))
first char is: {
getting ready to close stream on file deps.edn
nil
```

To sum up,

- Clojure is a LISP
- There's no syntax, only data structures
- We can easily extend the language with macros when we need to
- The workflow involves instant feedback from the running REPL

There's much more to Clojure itself:

- Mature APIs that operate on sequences (lists, vectors, maps, sets, records, etc.)
- Immutable data structures with APIs and underlying implementation to support their efficient usage
- Mature APIs that deal with concurrency, including software transactional memory (no locks, etc.)
- First-class interop with the host language: Java for Clojure, JavaScript for ClojureScript

Read more [here][clojure-rationale].

Of course, Clojure is not a magic bullet and has issues:

- Docs could be better
- Debugging is not always straightforward (doubly so for macros)
- Setting up a development environment takes a bit of work (see more below)
- Adoption in the overall development community is slow
- Error messages expect you to know quite a bit about the language at times

Despite them, I encourage you to give Clojure(Script) a shot.

#### REPL in Space

[Here][repl-in-space]'s a talk by Dr. Ron Garret on LISP at NASA and debugging a space craft million miles away from Earth using a REPL running on the space craft.

### Step 7: What's Next?

Setting up a proper development environment is crucial to a blissful Clojure(Script) experience. You'll be hooking up to running REPLs and evaluating your code straight from the editor! Here are some options:

- IntelliJ + [Cursive][]
- VSCode + [Calva][]
- Emacs + [CIDER][]
- Vim + [Fireplace][]
- Atom + [Proto REPL][]
- Sublime + [SublimeREPL][]

You probably see one of your favourite editors in this list -- go with it! I've been using IntelliJ and Emacs -- both work great. I've heard good things about VSCode and Vim, too.

If you, like me, learn by reading books, try [Clojure for the Brave and True][] and [Programming Clojure][].

For next steps in front end with ClojureScript, one of the more popular options is [re-frame][].

### Step 8: Addendum

There are some thing I'd like to add to this ClojureScript template we've been working on. They are not important enough to be highlighted above, but deserve a mention nonetheless.

- `deps.edn`

Adding a new dependency: `binaryage/devtools {:mvn/version "1.0.0"}`.
It will make ClojureScript values we output to the Chrome DevTools console look nicer.

- `src/dev/utils.clj`

```clojure
(ns utils
  (:require [shadow.cljs.devtools.api :as shadow]))

(defn watch-and-jack-in-app []
  (shadow/watch :app)
  (shadow/repl :app))
```

It's nice to have a helper function.

- `shadow-cljs.edn`

```clojure
 :nrepl
 {:port 9001
  :init-ns utils}
```

Have our server nREPL always start on port 9001 and switch to `utils` namespace we just defined.


### Clone it!

The final result is on [GitHub][clojurescript-template]. Hack away!



[clojurescript-template]: https://github.com/danplisetsky/clojurescript-template
[cljs-example]: https://github.com/danplisetsky/cljs-example
[cljs-example-live]: https://danplisetsky.github.io/cljs-example/
[official ClojureScript]: https://clojurescript.org/guides/quick-start
[node-npm]: https://nodejs.org/en/
[Clojure]: https://clojure.org/guides/getting_started
[brew]: https://brew.sh
[shadow-cljs]: https://shadow-cljs.org
[Deps]: https://clojure.org/guides/deps_and_cli
[target-browser]: https://shadow-cljs.github.io/docs/UsersGuide.html#target-browser
[Reagent]: https://reagent-project.github.io
[fully persistent]: https://en.m.wikipedia.org/wiki/Persistent_data_structure
[clojure.lang.PersistentList]: https://github.com/clojure/clojure/blob/master/src/jvm/clojure/lang/PersistentList.java
[using]: https://docs.microsoft.com/en-us/dotnet/csharp/language-reference/keywords/using-statement
[using-remarks]: https://docs.microsoft.com/en-us/dotnet/csharp/language-reference/keywords/using-statement#remarks
[clojure-rationale]: https://clojure.org/about/rationale
[repl-in-space]: https://www.youtube.com/watch?v=_gZK0tW8EhQ
[Cursive]: https://cursive-ide.com
[Calva]: https://calva.readthedocs.io
[CIDER]: https://cider.mx
[Fireplace]: https://github.com/tpope/vim-fireplace
[Proto REPL]: https://atom.io/packages/proto-repl
[SublimeREPL]: https://packagecontrol.io/packages/SublimeREPL
[Clojure for the Brave and True]: https://www.braveclojure.com/clojure-for-the-brave-and-true
[Programming Clojure]: https://pragprog.com/book/shcloj3/programming-clojure-third-edition
[re-frame]: https://github.com/day8/re-frame