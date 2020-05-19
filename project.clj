(defproject locking_test "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.2-alpha1"]

                 [org.clojure/tools.cli "0.4.2"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/data.xml "0.2.0-alpha6"]
                 [markdown-clj "1.10.0"]

                 [io.forward/yaml "1.0.10"]

                 [cljstache "2.0.4"]
                 [enlive "1.1.6"]
                 [hickory "0.7.1"]
                 [selmer "1.12.17"]

                 ;; for clojure eval
                 [borkdude/sci "0.0.13-alpha.23"]

                 ;; clojures pprint doesn't work under graal native-image
                 [fipp "0.6.23"]
                 [mvxcvi/puget "1.2.0"]

                 ]
  :main ^:skip-aot locking-test.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
