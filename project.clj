(defproject locking_test "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.2-alpha1"]
                 [borkdude/sci "0.0.13-alpha.23"]]
  :java-source-paths ["src/java" "sci-reflector/src-java"]
  :main ^:skip-aot locking-test.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
