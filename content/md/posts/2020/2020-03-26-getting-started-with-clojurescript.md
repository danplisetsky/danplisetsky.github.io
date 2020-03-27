{:title "Getting Started with ClojureScript"
 :layout :post
 :tags ["ClojureScript"]
 :music {:artist "Morrissey"
 	 :song "I Wish You Lonely"}
 :book {:author "Fyodor Dostoevsky"
        :title "The Brothers Karamazov"}}

TLDR: clone this repo to get started, or this repo for a more involved example (the latter is also live here)

***

Clojure is famous for being amazing and a total bliss in every way... except when it comes to documentation. This is especially true of the [official ClojureScript][] website: the Quick Start guide doesn't even mention html, even though the idea is to compile ClojureScript to JavaScript and then reference it in an html file.

Let's see how we can build, step by step, a blissful ClojureScript development experience, and then we'll look at the benefits of using this weird, funny-looking, parenthesis-heavy (there's a huge point behind them, I swear! read on) language.

### Step 0: Prerequisites

You'll need the following tools available on your PATH:

- [Node/npm][node-npm]
- [Clojure][]

If you're on Mac, both are also available from [brew][].

### Step 1: HTML

We'll start with html.

```html

<body>
</body>
```

```javascript

console.log('hi')

```


[official ClojureScript]: https://clojurescript.org/guides/quick-start
[Node-npm]: https://nodejs.org/en/
[Clojure]: https://clojure.org/guides/getting_started
[brew]: https://brew.sh