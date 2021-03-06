package slamdata.engine.fs

import scalaz._
import Scalaz._

import org.specs2.mutable._

class PathSpecs extends Specification {
  "Path.apply" should {
    "Parse empty string as root" in {
      Path("") must_== Path.Root
    }

    "Parse root string as root" in {
      Path("/") must_== Path.Root
    }

    "Parse current as current" in {
      Path(".") must_== Path.Current
    }

    "Parse multiple slashes as root" in {
      Path("/////////////////////////////") must_== Path.Root
    }

    "Parse trailing slash as pure directory" in {
      Path("/foo/bar/baz/") must_== Path.dir("foo" :: "bar" :: "baz" :: Nil)
    }

    "Parse lack of trailing slash as file" in {
      Path("/foo/bar/baz") must_== Path.file("foo" :: "bar" :: Nil, "baz")
    }

    "Correctly parse root file" in {
      Path("/foo") must_== Path.file(Nil, "foo")
    }

    "Parse raw file as relative file" in {
      Path("foo") must_== Path.file("." :: Nil, "foo")
    }

    "Parse raw relative file as relative file" in {
      Path("./foo") must_== Path.file("." :: Nil, "foo")
    }

    "Parse raw directory as relative directory" in {
      Path("foo/") must_== Path.dir("." :: "foo" :: Nil)
    }

    "Parse raw relative directory as relative directory" in {
      Path("./foo/") must_== Path.dir("." :: "foo" :: Nil)
    }

    "Parse hidden file as hidden file" in {
      Path(".foo") must_== Path.file("." :: Nil, ".foo")
    }

    "Parse hidden directory as hidden directory" in {
      Path(".foo/") must_== Path.dir("." :: ".foo" :: Nil)
    }
  }

  "Path.pathname" should {
    "render root correctly" in {
      Path.Root.pathname must_== "/"
    }

    "render current correctly" in {
      Path.Current.pathname must_== "./"
    }

    "render absolute pure dir correctly" in {
      Path("/foo/bar/baz/").pathname must_== "/foo/bar/baz/"
    }

    "render absolute file correctly" in {
      Path("/foo/bar/baz").pathname must_== "/foo/bar/baz"
    }

    "render relative pure dir correctly" in {
      Path("./foo/bar/baz/").pathname must_== "./foo/bar/baz/"
    }

    "render relative file correctly" in {
      Path("./foo/bar/baz").pathname must_== "./foo/bar/baz"
    }
  }

  "Path.relative" should {
    "be false for absolute path" in {
      Path("/foo").relative must beFalse
    }

    "be true for relative path" in {
      Path("./foo").relative must beTrue
    }
  }

  "Path.contains" should {
    "return true when parent contains child dir" in {
      Path("/foo/bar/").contains(Path("/foo/bar/baz/")) must beTrue
    }

    "return true when parent contains child file" in {
      Path("/foo/bar/").contains(Path("/foo/bar/baz")) must beTrue
    }

    "return true for abs path that contains itself" in {
      Path("/foo/bar/").contains(Path("/foo/bar/")) must beTrue
    }
  }
}