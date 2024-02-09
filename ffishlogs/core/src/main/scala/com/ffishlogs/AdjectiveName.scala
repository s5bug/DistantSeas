package com.ffishlogs

import java.util.random.{RandomGenerator, RandomGeneratorFactory}

object AdjectiveName {
  
  private[this] final val adjectives: Array[String] = Array(
    "Admiring",
    "Adoring",
    "Affectionate",
    "Agitated",
    "Amazing",
    "Angry",
    "Awesome",
    "Beautiful",
    "Blissful",
    "Bold",
    "Boring",
    "Brave",
    "Busy",
    "Charming",
    "Clever",
    "Compassionate",
    "Competent",
    "Condescending",
    "Confident",
    "Cool",
    "Cranky",
    "Crazy",
    "Dazzling",
    "Determined",
    "Distracted",
    "Dreamy",
    "Eager",
    "Ecstatic",
    "Elastic",
    "Elated",
    "Elegant",
    "Eloquent",
    "Epic",
    "Exciting",
    "Fervent",
    "Festive",
    "Flamboyant",
    "Focused",
    "Friendly",
    "Frosty",
    "Funny",
    "Gallant",
    "Gifted",
    "Goofy",
    "Gracious",
    "Great",
    "Happy",
    "Hardcore",
    "Heuristic",
    "Hopeful",
    "Hungry",
    "Infallible",
    "Inspiring",
    "Intelligent",
    "Interesting",
    "Jolly",
    "Jovial",
    "Keen",
    "Kind",
    "Laughing",
    "Loving",
    "Lucid",
    "Magical",
    "Modest",
    "Musing",
    "Mystifying",
    "Naughty",
    "Nervous",
    "Nice",
    "Nifty",
    "Nostalgic",
    "Objective",
    "Optimistic",
    "Peaceful",
    "Pedantic",
    "Pensive",
    "Practical",
    "Priceless",
    "Quirky",
    "Quizzical",
    "Recursing",
    "Relaxed",
    "Reverent",
    "Romantic",
    "Sad",
    "Serene",
    "Sharp",
    "Silly",
    "Sleepy",
    "Stoic",
    "Strange",
    "Stupefied",
    "Suspicious",
    "Sweet",
    "Tender",
    "Thirsty",
    "Trusting",
    "Unruffled",
    "Upbeat",
    "Vibrant",
    "Vigilant",
    "Vigorous",
    "Wizardly",
    "Wonderful",
    "Xenodochial",
    "Youthful",
    "Zealous",
    "Zen",
  )
  
  private[this] final val names: Array[String] = Array(
    "Adamantoise",
    "Anole",
    "Antelope",
    "Balloon",
    "Banemite",
    "Bat",
    "Blastmaster",
    "Boar",
    "Boldwing",
    "Bones",
    "Buzzard",
    "Chanter",
    "Chigoe",
    "Coeurl",
    "Cogfinder",
    "Deathgaze",
    "Decurion",
    "Diremite",
    // "Djigga", Too close to a slur to be included in the working list
    "Dryad",
    "Dullahan",
    "Engineer",
    "Eques",
    "Flan",
    "Fogcaller",
    "Funguar",
    "Gigantoad",
    "Gnat",
    "Goblin",
    "Golem",
    "Gunner",
    "Gutter",
    "Hare",
    "Hivekeep",
    "Hog",
    "Hoglet",
    "Hoplomachus",
    "Hornet",
    "Imp",
    "Ked",
    "Kedtrap",
    "Ladybug",
    "Laquearius",
    "Lemur",
    "Lodesman",
    "Mage",
    "Mandragora",
    "Microchu",
    "Mindflayer",
    "Miteling",
    "Nailfinder",
    "Nix",
    "Ochu",
    "Opo-opo",
    "Overseer",
    "Piledriver",
    "Plasmoid",
    "Priest",
    "Pteroc",
    "Pudding",
    "Roselet",
    "Screech",
    "Secutor",
    "Sharpeye",
    "Shockblocker",
    "Sigh",
    "Signifier",
    "Slug",
    "Snarl",
    "Song",
    "Spriggan",
    "Sprite",
    "Squirrel",
    "Stonehauler",
    "Strutfinder",
    "Swiftbeak",
    "Swordfighter",
    "Syrphid",
    "Toadstool",
    "Tonberry",
    "Treant",
    "Vulture",
    "Waister",
    "Weevil",
    "Yarzon",
    "Zombie"
  )
  
  def apply(uid: Long): String = {
    val g: RandomGenerator = RandomGeneratorFactory.getDefault.create(uid)
    val adjectiveIndex = g.nextInt(adjectives.length)
    val nameIndex = g.nextInt(names.length)
    s"${adjectives(adjectiveIndex)} ${names(nameIndex)}"
  }

}
