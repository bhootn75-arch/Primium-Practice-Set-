package com.example.data.seed

import com.example.data.AppDatabase
import com.example.data.model.PracticeSetEntity
import com.example.data.model.QuestionEntity

object DefaultPracticeSets {
  suspend fun populateInitialData(database: AppDatabase) {
    val setDao = database.practiceSetDao()
    val questionDao = database.questionDao()

    // 1. Santali Language & Ol Chiki Script Foundation
    val set1Id = setDao.insertSet(
      PracticeSetEntity(
        title = "ᱥᱟᱱᱛᱟᱲᱤ ᱯᱟᱹᱨᱥᱤ ᱟᱨ ᱚᱞ ᱪᱤᱠᱤ (Santali & Ol Chiki)",
        subject = "Ol Chiki Special",
        description = "Santali language origin, Pandit Raghunath Murmu, Ol Chiki alphabets, phonetics & basic grammar practice set.",
        author = "Jagu Sir (Santali Smart Study)",
        durationMinutes = 15,
        marksPerQuestion = 2.0f,
        negativeMarking = 0.5f,
        isFavorite = true,
        totalQuestionsCount = 10
      )
    )

    val questions1 = listOf(
      QuestionEntity(
        setId = set1Id,
        questionNumber = 1,
        questionText = "ᱚᱞ ᱪᱤᱠᱤ ᱞᱤᱯᱤ ᱨᱤᱱᱤᱡ ᱥᱤᱨᱡᱟᱹᱱᱤᱭᱟᱹ (Creator) ᱫᱚ ᱚᱠᱚᱭ ᱠᱟᱱᱟᱭ?\n(Ol Chiki lipi ke srijankarta kaun hain?)",
        optionA = "ᱯᱚᱸᱰᱮᱛ ᱨᱟᱹᱜᱷᱩᱱᱟᱛᱷ ᱢᱩᱨᱢᱩ (Pandit Raghunath Murmu)",
        optionB = "ᱥᱤᱫᱷᱩ ᱠᱟᱹᱱᱦᱩ (Sidhu Kanhu)",
        optionC = "ᱵᱤᱨᱥᱟ ᱢᱩᱱᱰᱟ (Birsa Munda)",
        optionD = "ᱥᱟᱫᱷᱩ ᱨᱟᱢᱪᱟᱸᱫᱽ ᱢᱩᱨᱢᱩ (Sadhu Ramchand Murmu)",
        correctOption = "A",
        explanation = "ᱚᱞ ᱪᱤᱠᱤ ᱞᱤᱯᱤ ᱫᱚ ᱯᱚᱸᱰᱮᱛ ᱨᱟᱹᱜᱷᱩᱱᱟᱛᱷ ᱢᱩᱨᱢᱩ ᱛᱤᱠᱤᱱ ᱑᱙᱒᱕ (1925) ᱥᱟᱞᱮ ᱨᱮ ᱠᱤᱱ ᱥᱤᱨᱡᱟᱹᱣ ᱞᱮᱫ-ᱟ᱾",
        scriptTag = "Ol Chiki & Hindi"
      ),
      QuestionEntity(
        setId = set1Id,
        questionNumber = 2,
        questionText = "ᱚᱞ ᱪᱤᱠᱤ ᱨᱮ ᱜᱩᱴ ᱛᱤᱱᱟᱹᱜ ᱟᱠᱷᱚᱨ (Letters) ᱢᱮᱱᱟᱜ-ᱟ?\n(Ol Chiki lipi mein kul kitne varn / letters hain?)",
        optionA = "᱒᱕ (25)",
        optionB = "᱓᱐ (30)",
        optionC = "᱓᱕ (35)",
        optionD = "᱔᱐ (40)",
        correctOption = "B",
        explanation = "ᱚᱞ ᱪᱤᱠᱤ ᱨᱮ ᱢᱩᱬᱩᱛ ᱓᱐ (30) ᱜᱚᱴᱟᱝ ᱟᱠᱷᱚᱨ ᱢᱮᱱᱟᱜ-ᱟ, ᱡᱟᱦᱟᱸ ᱫᱚ ᱖ ᱜᱚᱴᱟᱝ ᱛᱷᱚᱠ (Rows) ᱨᱮ ᱦᱟᱹᱴᱤᱧ ᱟᱠᱟᱱᱟ᱾",
        scriptTag = "Ol Chiki & Hindi"
      ),
      QuestionEntity(
        setId = set1Id,
        questionNumber = 3,
        questionText = "ᱥᱟᱱᱛᱟᱲᱤ ᱯᱟᱹᱨᱥᱤ ᱫᱚ ᱥᱤᱧᱚᱛ ᱥᱚᱝᱵᱤᱫᱷᱟᱱ ᱨᱮᱱᱟᱜ ᱚᱠᱟ ᱛᱷᱚᱠ (8th Schedule) ᱨᱮ ᱥᱮᱞᱮᱫ ᱟᱠᱟᱱᱟ?\n(Santali bhasha ko Sanvidhan ki 8vi Anusuchi mein kab shamil kiya gaya?)",
        optionA = "᱒᱐᱐᱐ (2000)",
        optionB = "᱒᱐᱐᱓ (2003 - 92nd Amendment)",
        optionC = "᱒᱐᱐᱕ (2005)",
        optionD = "᱒᱐᱑᱐ (2010)",
        correctOption = "B",
        explanation = "ᱥᱟᱱᱛᱟᱲᱤ ᱯᱟᱹᱨᱥᱤ ᱫᱚ 92nd Constitutional Amendment Act, 2003 ᱞᱮᱠᱟᱛᱮ ᱘ ᱟᱱᱟᱜ ᱛᱷᱚᱠ ᱨᱮ ᱥᱮᱞᱮᱫ ᱞᱮᱱᱟ᱾",
        scriptTag = "Ol Chiki & Hindi"
      ),
      QuestionEntity(
        setId = set1Id,
        questionNumber = 4,
        questionText = "ᱚᱞ ᱪᱤᱠᱤ ᱨᱮ 'ᱸ' (Mu-tuduag) ᱨᱮᱱᱟᱜ ᱠᱟᱹᱢᱤ ᱪᱮᱫ ᱠᱟᱱᱟ?\n(Ol Chiki mein Mu-tuduag ka prayog kisliye hota hai?)",
        optionA = "ᱱᱟᱥᱤᱠᱭᱚ ᱥᱟᱰᱮ (Nasalization - ᱟᱸ/Anuswar)",
        optionB = "ᱦᱟᱹᱲᱩᱵ ᱥᱟᱰᱮ (Glottal stop)",
        optionC = "ᱠᱮᱪᱮᱫ ᱟᱲᱟᱝ (Consonant stop)",
        optionD = "ᱡᱤᱞᱤᱧ ᱟᱲᱟᱝ (Vowel lengthener)",
        correctOption = "A",
        explanation = "ᱸ (Mu-tuduag) ᱫᱚ ᱟᱠᱷᱚᱨ ᱪᱮᱛᱟᱱ ᱨᱮ ᱞᱟᱜᱟᱣ ᱠᱟᱛᱮ ᱢᱩᱸ ᱛᱮ ᱥᱟᱰᱮ (Nasal sound) ᱚᱰᱚᱠ ᱞᱟᱹᱜᱤᱫ ᱵᱮᱵᱷᱟᱨᱚᱜ-ᱟ᱾",
        scriptTag = "Ol Chiki"
      ),
      QuestionEntity(
        setId = set1Id,
        questionNumber = 5,
        questionText = "ᱯᱚᱸᱰᱮᱛ ᱨᱟᱹᱜᱷᱩᱱᱟᱛᱷ ᱢᱩᱨᱢᱩ ᱣᱟᱜ ᱡᱟᱱᱟᱢ ᱢᱟᱦᱟᱸ ᱫᱚ ᱛᱤᱥ ᱠᱟᱱᱟ?\n(Pandit Raghunath Murmu ji ka janm divas kab hai?)",
        optionA = "᱕ ᱢᱮ (5th May 1905)",
        optionB = "᱑᱕ ᱱᱚᱵᱷᱮᱢᱵᱚᱨ (15th Nov)",
        optionC = "᱓᱐ ᱡᱩᱱ (30th June)",
        optionD = "᱒ ᱚᱠᱴᱚᱵᱚᱨ (2nd Oct)",
        correctOption = "A",
        explanation = "ᱯᱚᱸᱰᱮᱛ ᱨᱟᱹᱜᱷᱩᱱᱟᱛᱷ ᱢᱩᱨᱢᱩ ᱛᱤᱠᱤᱱᱟᱜ ᱡᱟᱱᱟᱢ ᱫᱚ ᱕ ᱢᱮ ᱑᱙᱐᱕ (May 5, 1905) ᱢᱚᱭᱩᱨᱵᱷᱚᱸᱡᱽ ᱨᱮ ᱦᱩᱭ ᱞᱮᱱᱟ᱾",
        scriptTag = "Ol Chiki & Hindi"
      ),
      QuestionEntity(
        setId = set1Id,
        questionNumber = 6,
        questionText = "ᱥᱟᱱᱛᱟᱲᱤ ᱯᱟᱹᱨᱥᱤ ᱫᱚ ᱚᱠᱟ ᱯᱟᱹᱨᱥᱤ ᱜᱷᱟᱨᱚᱸᱡᱽ (Language Family) ᱨᱮᱱᱟᱜ ᱠᱟᱱᱟ?\n(Santali bhasha kis bhasha parivar se sambandhit hai?)",
        optionA = "ᱚᱥᱴᱨᱳ-ᱮᱥᱤᱭᱟᱴᱤᱠ / ᱚᱥᱴᱨᱤᱠ (Austroasiatic / Munda family)",
        optionB = "ᱫᱨᱟᱵᱷᱤᱰᱤᱭᱟᱱ (Dravidian)",
        optionC = "ᱤᱱᱰᱳ-ᱟᱨᱭᱟᱱ (Indo-Aryan)",
        optionD = "ᱛᱤᱵᱵᱚᱛᱳ-ᱵᱚᱨᱢᱟᱱ (Tibeto-Burman)",
        correctOption = "A",
        explanation = "ᱥᱟᱱᱛᱟᱲᱤ ᱯᱟᱹᱨᱥᱤ ᱫᱚ Austroasiatic ᱜᱷᱟᱨᱚᱸᱡᱽ ᱨᱮᱱᱟᱜ ᱢᱩᱱᱰᱟ ᱦᱟᱹᱴᱤᱧ ᱠᱟᱱᱟ᱾",
        scriptTag = "Ol Chiki & English"
      ),
      QuestionEntity(
        setId = set1Id,
        questionNumber = 7,
        questionText = "'ᱵᱤᱫᱩ ᱪᱟᱸᱫᱟᱱ' (Bidu Chandan) ᱯᱩᱛᱷᱤ ᱫᱚ ᱚᱠᱚᱭ ᱮ ᱚᱞ ᱟᱠᱟᱫ-ᱟ?\n('Bidu Chandan' pustak kiske dwara likhi gayi hai?)",
        optionA = "ᱯᱚᱸᱰᱮᱛ ᱨᱟᱹᱜᱷᱩᱱᱟᱛᱷ ᱢᱩᱨᱢᱩ (Pandit Raghunath Murmu)",
        optionB = "ᱠᱟᱹᱵᱤ ᱥᱟᱫᱷᱩ ᱨᱟᱢᱪᱟᱸᱫᱽ ᱢᱩᱨᱢᱩ",
        optionC = "ᱰᱨ. ᱰᱳᱢᱚᱱ ᱥᱟᱦᱩ 'ᱥᱚᱢᱤᱨ'",
        optionD = "ᱥᱟᱨᱫᱟ ᱯᱨᱚᱥᱟᱫᱽ ᱠᱤᱥᱠᱩ",
        correctOption = "A",
        explanation = "'ᱵᱤᱫᱩ ᱪᱟᱸᱫᱟᱱ' ᱫᱚ ᱯᱚᱸᱰᱮᱛ ᱨᱟᱹᱜᱷᱩᱱᱟᱛᱷ ᱢᱩᱨᱢᱩ ᱛᱤᱠᱤᱱᱟᱜ ᱟᱹᱰᱤ ᱧᱩᱛᱩᱢᱟᱱ ᱜᱟᱭᱟᱱ (Play) ᱠᱟᱱᱟ᱾",
        scriptTag = "Ol Chiki & Hindi"
      ),
      QuestionEntity(
        setId = set1Id,
        questionNumber = 8,
        questionText = "ᱚᱞ ᱪᱤᱠᱤ ᱨᱮ ᱛᱤᱱᱟᱹᱜ ᱨᱟᱦᱟ ᱟᱲᱟᱝ (Vowels) ᱢᱮᱱᱟᱜ-ᱟ?\n(Ol Chiki lipi mein kitne Rah-a-alang / Vowels hain?)",
        optionA = "᱕ (5)",
        optionB = "᱖ (6 - ᱚ, ᱟ, ᱤ, ᱩ, ᱮ, ᱳ)",
        optionC = "᱘ (8)",
        optionD = "᱑᱐ (10)",
        correctOption = "B",
        explanation = "ᱚᱞ ᱪᱤᱠᱤ ᱨᱮ ᱖ ᱜᱚᱴᱟᱝ ᱨᱟᱦᱟ ᱟᱲᱟᱝ (Vowels) ᱢᱮᱱᱟᱜ-ᱟ: ᱚ (La), ᱟ (A), ᱤ (I), ᱩ (U), ᱮ (E), ᱳ (O)᱾",
        scriptTag = "Ol Chiki"
      ),
      QuestionEntity(
        setId = set1Id,
        questionNumber = 9,
        questionText = "ᱥᱟᱱᱛᱟᱲᱤ ᱥᱟᱶᱦᱮᱫ ᱨᱮᱱ 'ᱵᱷᱟᱸᱡᱽ ᱵᱤᱨ' (Bhanj Bir) ᱢᱮᱱᱛᱮ ᱚᱠᱚᱭ ᱠᱚ ᱢᱮᱛᱟᱭᱟ?\n(Santali Sahitya mein 'Bhanj Bir' ke naam se kise jana jata hai?)",
        optionA = "ᱥᱩᱱᱟᱨᱟᱢ ᱥᱚᱨᱮᱱ (Sunaram Soren)",
        optionB = "ᱯᱚᱸᱰᱮᱛ ᱨᱟᱹᱜᱷᱩᱱᱟᱛᱷ ᱢᱩᱨᱢᱩ",
        optionC = "ᱥᱟᱨᱫᱟ ᱯᱨᱚᱥᱟᱫᱽ ᱠᱤᱥᱠᱩ",
        optionD = "ᱜᱳᱨᱟᱪᱟᱸᱫᱽ ᱴᱩᱰᱩ",
        correctOption = "A",
        explanation = "ᱥᱩᱱᱟᱨᱟᱢ ᱥᱚᱨᱮᱱ ᱫᱚ 'ᱵᱷᱟᱸᱡᱽ ᱵᱤᱨ' ᱢᱮᱱᱛᱮ ᱵᱟᱰᱟᱭᱚᱜ-ᱟᱭ, ᱟᱨ ᱨᱟᱹᱜᱷᱩᱱᱟᱛᱷ ᱢᱩᱨᱢᱩ ᱫᱚ 'ᱜᱩᱨᱩ ᱜᱚᱢᱠᱮ' ᱢᱮᱱᱛᱮ ᱠᱚ ᱢᱟᱱᱟᱣᱮᱭᱟ᱾",
        scriptTag = "Ol Chiki & Hindi"
      ),
      QuestionEntity(
        setId = set1Id,
        questionNumber = 10,
        questionText = "ᱥᱟᱱᱛᱟᱲᱤ ᱯᱟᱹᱨᱥᱤ ᱛᱮ ᱯᱩᱭᱞᱩ ᱥᱟᱦᱤᱛᱭᱚ ᱮᱠᱟᱰᱮᱢᱤ ᱥᱤᱨᱯᱷᱟᱹ (Sahitya Akademi Award) ᱚᱠᱚᱭ ᱧᱟᱢ ᱞᱮᱫ-ᱟ?\n(Santali bhasha mein pehla Sahitya Akademi Puraskar kise mila?)",
        optionA = "ᱡᱟᱫᱩᱢᱚᱬᱤ ᱵᱮᱥᱨᱟ (Jadumani Besra - 2005)",
        optionB = "ᱨᱚᱵᱤᱞᱟᱞ ᱴᱩᱰᱩ",
        optionC = "ᱵᱷᱳᱜᱽᱞᱟ ᱥᱚᱨᱮᱱ",
        optionD = "ᱫᱟᱢᱚᱭᱚᱱᱛᱤ ᱵᱮᱥᱨᱟ",
        correctOption = "A",
        explanation = "᱒᱐᱐᱕ ᱥᱟᱞᱮ ᱨᱮ ᱡᱟᱫᱩᱢᱚᱬᱤ ᱵᱮᱥᱨᱟ ᱛᱤᱠᱤᱱ 'ᱵᱷᱟᱵᱱᱟ' (Bhabna) ᱚᱱᱚᱬᱦᱮ ᱯᱩᱛᱷᱤ ᱞᱟᱹᱜᱤᱫ ᱯᱩᱭᱞᱩ ᱥᱤᱨᱯᱷᱟᱹ ᱠᱤᱱ ᱧᱟᱢ ᱞᱮᱫ-ᱟ᱾",
        scriptTag = "Ol Chiki & Hindi"
      )
    )
    questionDao.insertQuestions(questions1)

    // 2. Jharkhand GK & Santal Pargana Special
    val set2Id = setDao.insertSet(
      PracticeSetEntity(
        title = "Jharkhand GK & Santal Pargana Special (ᱡᱷᱟᱨᱠᱷᱚᱸᱰ ᱵᱤᱥᱮᱥ)",
        subject = "Jharkhand GK",
        description = "Santal Rebellion (Hul), Sidhu Kanhu, historical movements, geography & cultural heritage by Jagu Sir.",
        author = "Jagu Sir (Santali Smart Study)",
        durationMinutes = 15,
        marksPerQuestion = 2.0f,
        negativeMarking = 0.5f,
        isFavorite = false,
        totalQuestionsCount = 10
      )
    )

    val questions2 = listOf(
      QuestionEntity(
        setId = set2Id,
        questionNumber = 1,
        questionText = "ᱥᱟᱱᱛᱟᱲ ᱦᱩᱞ (Santal Hul / Rebellion) ᱫᱚ ᱛᱤᱥ ᱮᱦᱚᱵ ᱞᱮᱱᱟ?\n(Santal Hul vidroh kis varsh shuru hua tha?)",
        optionA = "᱓᱐ ᱡᱩᱱ ᱑᱘᱕᱕ (30 June 1855)",
        optionB = "᱑᱕ ᱱᱚᱵᱷᱮᱢᱵᱚᱨ ᱑᱘᱕᱗",
        optionC = "᱑᱐ ᱢᱮ ᱑᱘᱕᱗",
        optionD = "᱒᱖ ᱡᱟᱱᱩᱣᱟᱨᱤ ᱑᱘᱕᱐",
        correctOption = "A",
        explanation = "ᱥᱟᱱᱛᱟᱲ ᱦᱩᱞ ᱫᱚ ᱓᱐ ᱡᱩᱱ ᱑᱘Runner ᱥᱟᱞ ᱵᱷᱚᱜᱽᱱᱟᱰᱤᱦ (Bhognadih) ᱠᱷᱚᱱ ᱥᱤᱫᱷᱩ-ᱠᱟᱹᱱᱦᱩ ᱟᱜ ᱟᱹᱭᱩᱨ ᱛᱮ ᱮᱦᱚᱵ ᱞᱮᱱᱟ᱾ ᱚᱱᱟᱛᱮ ᱓᱐ ᱡᱩᱱ ᱫᱚ 'ᱦᱩᱞ ᱢᱟᱦᱟᱸ' (Hul Diwas) ᱢᱮᱱᱛᱮ ᱵᱚᱱ ᱢᱟᱱᱟᱣᱟ᱾",
        scriptTag = "Ol Chiki & Hindi"
      ),
      QuestionEntity(
        setId = set2Id,
        questionNumber = 2,
        questionText = "ᱥᱟᱱᱛᱟᱲ ᱦᱩᱞ ᱨᱤᱱ ᱢᱩᱬᱩᱛ ᱵᱤᱨ ᱟᱹᱭᱩᱨᱤᱭᱟᱹ (Leaders) ᱚᱠᱚᱭ ᱠᱚ ᱠᱚ ᱛᱟᱦᱮᱸ ᱠᱟᱱᱟ?\n(Santal Hul ke pramukh netritvakarta kaun the?)",
        optionA = "ᱥᱤᱫᱷᱩ, ᱠᱟᱹᱱᱦᱩ, ᱪᱟᱸᱫᱽ, ᱵᱷᱟᱭᱨᱚ ᱟᱨ ᱯᱷᱩᱞᱳ-ᱡᱷᱟᱱᱳ",
        optionB = "ᱵᱤᱨᱥᱟ ᱢᱩᱱᱰᱟ ᱟᱨ ᱜᱟᱭᱟ ᱢᱩᱱᱰᱟ",
        optionC = "ᱛᱤᱞᱠᱟᱹ ᱢᱟᱹᱡᱷᱤ ᱟᱨ ᱡᱚᱵᱽᱨᱟ ᱯᱟᱦᱟᱲᱤᱭᱟ",
        optionD = "ᱡᱟᱛᱨᱟ ᱵᱷᱚᱜᱚᱛ ᱟᱨ ᱵᱩᱫᱷᱩ ᱵᱷᱚᱜᱚᱛ",
        correctOption = "A",
        explanation = "ᱥᱤᱫᱷᱩ ᱠᱟᱹᱱᱦᱩ ᱢᱩᱨᱢᱩ, ᱪᱟᱸᱫᱽ-ᱵᱷᱟᱭᱨᱚ ᱟᱨ ᱩᱱᱠᱤᱱ ᱨᱤᱱ ᱢᱤᱥᱨᱟ ᱯᱷᱩᱞᱳ-ᱡᱷᱟᱱᱳ ᱦᱩᱞ ᱨᱮ ᱢᱟᱦᱟᱱ ᱮᱱᱮᱢ ᱠᱚ ᱮᱢ ᱞᱮᱫ-ᱟ᱾",
        scriptTag = "Ol Chiki & Hindi"
      ),
      QuestionEntity(
        setId = set2Id,
        questionNumber = 3,
        questionText = "ᱥᱤᱧᱚᱛ ᱨᱤᱱᱤᱡ ᱯᱩᱭᱞᱩ ᱟᱹᱫᱤᱵᱟᱹᱥᱤ ᱵᱤᱫᱽᱨᱳᱦᱤ 'ᱵᱟᱵᱟ ᱛᱤᱞᱠᱟᱹ ᱢᱟᱹᱡᱷᱤ' ᱫᱚ ᱚᱠᱟ ᱥᱟᱞ ᱨᱮ ᱥᱚᱦᱤᱫᱽ ᱞᱮᱱᱟᱭ?\n(Baba Tilka Majhi kis varsh shaheed hue the?)",
        optionA = "᱑᱗᱘᱕ (1785)",
        optionB = "᱑᱘᱕᱕ (1855)",
        optionC = "᱑᱘᱓᱑ (1831)",
        optionD = "᱑᱗᱙᱙ (1799)",
        correctOption = "A",
        explanation = "ᱵᱟᱵᱟ ᱛᱤᱞᱠᱟᱹ ᱢᱟᱹᱡᱷᱤ ᱫᱚ ᱑᱗᱘᱕ ᱥᱟᱞᱮ ᱨᱮ ᱵᱷᱟᱜᱚᱞᱯᱩᱨ ᱨᱮ ᱤᱝᱨᱮᱡᱽ ᱠᱚ ᱵᱚᱲ ᱫᱟᱨᱮ ᱨᱮ ᱠᱚ ᱯᱷᱟᱹᱥᱤ ᱞᱮᱫᱮᱭᱟ᱾",
        scriptTag = "Ol Chiki & Hindi"
      ),
      QuestionEntity(
        setId = set2Id,
        questionNumber = 4,
        questionText = "ᱥᱟᱱᱛᱟᱲ ᱯᱟᱨᱜᱟᱱᱟ ᱴᱮᱱᱮᱱᱥᱤ ᱮᱠᱴ (SPT Act) ᱫᱚ ᱛᱤᱥ ᱞᱟᱜᱩ ᱞᱮᱱᱟ?\n(Santal Pargana Tenancy Act kab lagu hua tha?)",
        optionA = "᱑᱙᱔᱙ (1949)",
        optionB = "᱑᱙᱐᱘ (1908)",
        optionC = "᱑᱘᱗᱖ (1876)",
        optionD = "᱑᱙᱕᱖ (1956)",
        correctOption = "A",
        explanation = "SPT Act ᱫᱚ ᱑᱙᱔᱙ (1949) ᱥᱟᱞᱮ ᱨᱮ ᱥᱟᱱᱛᱟᱲ ᱯᱟᱨᱜᱟᱱᱟ ᱨᱮ ᱟᱹᱫᱤᱵᱟᱹᱥᱤ ᱡᱟᱭᱜᱟ ᱨᱩᱠᱷᱤᱭᱟᱹ ᱞᱟᱹᱜᱤᱫ ᱞᱟᱜᱩ ᱞᱮᱱᱟ᱾",
        scriptTag = "Ol Chiki & Hindi"
      ),
      QuestionEntity(
        setId = set2Id,
        questionNumber = 5,
        questionText = "ᱥᱟᱱᱛᱟᱲ ᱠᱚᱣᱟᱜ ᱥᱮᱬᱟ ᱯᱚᱨᱚᱵᱽ 'ᱥᱚᱦᱨᱟᱭ' (Sohrai Festival) ᱫᱚ ᱚᱠᱟ ᱪᱟᱸᱫᱚ ᱨᱮ ᱠᱚ ᱢᱟᱱᱟᱣᱟ?\n(Santal samaj ka mukhya parv Sohrai kis mahine manaya jata hai?)",
        optionA = "ᱥᱚᱦᱨᱟᱭ / ᱠᱟᱨᱛᱤᱠ-ᱯᱩᱥ ᱪᱟᱸᱫᱚ (Kartik/Poush Month - Harvest season)",
        optionB = "ᱪᱟᱹᱛ ᱪᱟᱸᱫᱚ (Chaitra)",
        optionC = "ᱵᱟᱹᱭᱥᱟᱹᱠ (Baisakh)",
        optionD = "ᱥᱟᱣᱩᱱ (Shravan)",
        correctOption = "A",
        explanation = "ᱥᱚᱦᱨᱟᱭ ᱫᱚ ᱦᱳᱲᱳ ᱤᱨ ᱟᱨ ᱜᱟᱹᱭ-ᱰᱟᱝᱜᱽᱨᱟ ᱠᱚᱣᱟᱜ ᱢᱟᱱᱚᱛ ᱮᱢ ᱞᱟᱹᱜᱤᱫ ၅ ᱢᱟᱦᱟᱸ ᱦᱟᱹᱵᱤᱡ ᱨᱟᱹᱥᱠᱟᱹ ᱛᱮ ᱠᱚ ᱢᱟᱱᱟᱣᱟ᱾",
        scriptTag = "Ol Chiki & Hindi"
      ),
      QuestionEntity(
        setId = set2Id,
        questionNumber = 6,
        questionText = "ᱡᱷᱟᱨᱠᱷᱚᱸᱰ ᱨᱮᱱᱟᱜ ᱨᱟᱡᱽᱜᱟᱲ ᱫᱚ ᱚᱠᱟ ᱠᱟᱱᱟ?\n(Jharkhand ki rajdhani kaun si hai?)",
        optionA = "ᱨᱟᱺᱪᱤ (Ranchi)",
        optionB = "ᱫᱩᱢᱠᱟᱹ (Dumka)",
        optionC = "ᱡᱟᱢᱥᱮᱫᱽᱯᱩᱨ (Jamshedpur)",
        optionD = "ᱫᱷᱟᱱᱵᱟᱫᱽ (Dhanbad)",
        correctOption = "A",
        explanation = "ᱨᱟᱺᱪᱤ ᱫᱚ ᱡᱷᱟᱨᱠᱷᱚᱸᱰ ᱨᱮᱱᱟᱜ ᱨᱟᱡᱽᱜᱟᱲ ᱠᱟᱱᱟ, ᱟᱨ ᱫᱩᱢᱠᱟᱹ ᱫᱚ ᱩᱯ-ᱨᱟᱡᱽᱜᱟᱲ (Sub-capital) ᱠᱟᱱᱟ᱾",
        scriptTag = "Ol Chiki & Hindi"
      ),
      QuestionEntity(
        setId = set2Id,
        questionNumber = 7,
        questionText = "ᱥᱟᱱᱛᱟᱲ ᱥᱟᱶᱛᱟ ᱨᱮ ᱟᱹᱛᱩ ᱨᱤᱱᱤᱡ ᱢᱩᱬᱩᱛ (Village Head) ᱫᱚ ᱪᱮᱫ ᱠᱚ ᱢᱮᱛᱟᱭᱟ?\n(Santal samaj mein gaon ke pradhan ko kya kehte hain?)",
        optionA = "ᱢᱟᱹᱡᱷᱤ ᱦᱟᱲᱟᱢ (Manjhi Haram)",
        optionB = "ᱱᱟᱭᱠᱮ (Naike)",
        optionC = "ᱜᱳᱰᱮᱛ (Godet)",
        optionD = "ᱯᱟᱨᱟᱱᱤᱠ (Paranik)",
        correctOption = "A",
        explanation = "ᱢᱟᱹᱡᱷᱤ ᱦᱟᱲᱟᱢ ᱫᱚ ᱟᱹᱛᱩ ᱨᱤᱱᱤᱡ ᱢᱩᱬᱩᱛ ᱠᱟᱱᱟᱭ, ᱱᱟᱭᱠᱮ ᱫᱚ ᱫᱷᱚᱨᱚᱢ ᱠᱟᱹᱢᱤ ᱟᱹᱭᱩᱨᱤᱭᱟᱹ ᱟᱨ ᱜᱳᱰᱮᱛ ᱫᱚ ᱠᱷᱚᱵᱚᱨᱤᱭᱟᱹ ᱠᱟᱱᱟᱭ᱾",
        scriptTag = "Ol Chiki & Hindi"
      ),
      QuestionEntity(
        setId = set2Id,
        questionNumber = 8,
        questionText = "ᱢᱟᱥᱟᱱᱡᱳᱨ ᱰᱮᱢ (Masanjor Dam) ᱫᱚ ᱚᱠᱟ ᱜᱟᱰᱟ ᱪᱮᱛᱟᱱ ᱨᱮ ᱵᱮᱱᱟᱣ ᱟᱠᱟᱱᱟ?\n(Masanjor Dam kis nadi par sthit hai?)",
        optionA = "ᱢᱚᱭᱩᱨᱟᱠᱷᱤ ᱜᱟᱰᱟ (Mayurakshi River - Dumka)",
        optionB = "ᱫᱟᱢᱳᱫᱚᱨ ᱜᱟᱰᱟ (Damodar River)",
        optionC = "ᱥᱚᱵᱚᱨᱱᱟᱠᱷᱟ ᱜᱟᱰᱟ (Subarnarekha River)",
        optionD = "ᱵᱚᱨᱟᱠᱚᱨ ᱜᱟᱰᱟ (Barakar River)",
        correctOption = "A",
        explanation = "ᱢᱟᱥᱟᱱᱡᱳᱨ ᱰᱮᱢ (Canada Dam) ᱫᱚ ᱫᱩᱢᱠᱟᱹ ᱡᱤᱞᱟᱹ ᱨᱮ ᱢᱚᱭᱩᱨᱟᱠᱷᱤ ᱜᱟᱰᱟ ᱪᱮᱛᱟᱱ ᱨᱮ ᱠᱟᱱᱟᱰᱟ ᱫᱤᱥᱚᱢ ᱜᱚᱲᱚ ᱛᱮ ᱵᱮᱱᱟᱣ ᱞᱮᱱᱟ᱾",
        scriptTag = "Ol Chiki & Hindi"
      ),
      QuestionEntity(
        setId = set2Id,
        questionNumber = 9,
        questionText = "ᱥᱟᱱᱛᱟᱲ ᱠᱚᱣᱟᱜ ᱫᱷᱚᱨᱚᱢ ᱛᱷᱟᱱ ᱫᱚ ᱪᱮᱫ ᱠᱚ ᱢᱮᱛᱟᱜ-ᱟ?\n(Santal samaj ke pavitra puja sthal ko kya kehte hain?)",
        optionA = "ᱡᱟᱦᱮᱨ ᱛᱷᱟᱱ (Jaher Than)",
        optionB = "ᱢᱟᱹᱡᱷᱤ ᱛᱷᱟᱱ (Manjhi Than)",
        optionC = "ᱥᱟᱥᱟᱱ ᱫᱷᱤᱨᱤ (Sasan)",
        optionD = "ᱜᱳᱥᱟᱬᱮ (Gosane)",
        correctOption = "A",
        explanation = "ᱡᱟᱦᱮᱨ ᱛᱷᱟᱱ ᱨᱮ ᱡᱟᱦᱮᱨ ᱮᱨᱟ, ᱢᱟᱨᱟᱝ ᱵᱳᱨᱳ ᱟᱨ ᱢᱳᱬᱮᱠᱳ-ᱛᱩᱨᱩᱭᱠᱳ ᱠᱚ ᱵᱚᱸᱜᱟ ᱦᱟᱠᱚᱣᱟ᱾",
        scriptTag = "Ol Chiki & Hindi"
      ),
      QuestionEntity(
        setId = set2Id,
        questionNumber = 10,
        questionText = "ᱥᱟᱱᱛᱟᱲᱤ ᱯᱟᱹᱨᱥᱤ ᱨᱮᱱᱟᱜ ᱯᱩᱭᱞᱩ ଖᱚᱵᱚᱨ ᱠᱟᱜᱚᱡᱽ (First Newspaper) ᱫᱚ ᱚᱠᱟ ᱛᱟᱦᱮᱸ ᱠᱟᱱᱟ?\n(Santali bhasha ka pehla samachar patra kaun sa tha?)",
        optionA = "ᱯᱮᱲᱟ ᱦᱚᱲ (Pera Hor - 1922)",
        optionB = "ᱦᱚᱲ ᱥᱚᱢᱵᱟᱫᱽ (Hor Sambad)",
        optionC = "ᱵᱚᱱᱰᱳ ᱵᱟᱱᱫᱷᱚᱵᱽ",
        optionD = "ᱟᱹᱛᱩ ᱠᱷᱚᱵᱚᱨ",
        correctOption = "A",
        explanation = "᱑᱙᱒᱒ ᱥᱟᱞ ᱨᱮ 'ᱯᱮᱲᱟ ᱦᱚᱲ' ᱫᱚ ᱯᱩᱭᱞᱩ ᱯᱟᱛᱷᱟᱢ ᱞᱮᱠᱟᱛᱮ ᱪᱷᱟᱯᱟ ᱞᱮᱱᱟ᱾ ᱛᱟᱭᱚᱢ ᱛᱮ 'ᱦᱚᱲ ᱥᱚᱢᱵᱟᱫᱽ' ᱫᱚ ᱑᱙᱔᱗ ᱨᱮ ᱫᱮᱵᱽᱱᱟᱜᱽᱨᱤ ᱛᱮ ᱩᱪᱷᱟᱹᱱ ᱮᱦᱚᱵ ᱞᱮᱱᱟ᱾",
        scriptTag = "Ol Chiki & Hindi"
      )
    )
    questionDao.insertQuestions(questions2)

    // 3. Santali Grammar & Literature (Ronor & Saonhed)
    val set3Id = setDao.insertSet(
      PracticeSetEntity(
        title = "Santali Grammar & Literature (ᱥᱟᱱᱛᱟᱲᱤ ᱨᱚᱱᱚᱲ ᱟᱨ ᱥᱟᱶᱦᱮᱫ)",
        subject = "Grammar & Literature",
        description = "Santali parts of speech, tenses, suffixes, proverbs (Kudum & Menkatha) and literary figures.",
        author = "Jagu Sir (Santali Smart Study)",
        durationMinutes = 15,
        marksPerQuestion = 2.0f,
        negativeMarking = 0.5f,
        isFavorite = true,
        totalQuestionsCount = 10
      )
    )

    val questions3 = listOf(
      QuestionEntity(
        setId = set3Id,
        questionNumber = 1,
        questionText = "ᱥᱟᱱᱛᱟᱲᱤ ᱨᱚᱱᱚᱲ (Grammar) ᱨᱮ 'ᱧᱩᱱᱩᱢ' (Noun) ᱨᱮᱱᱟᱜ ᱢᱮᱱᱮᱛ ᱫᱚ ᱪᱮᱫ ᱠᱟᱱᱟ?\n(Santali Vyakaran mein 'Nyunum' ka arth kya hai?)",
        optionA = "ᱡᱟᱦᱟᱸᱱᱟᱜ ᱧᱩᱛᱩᱢ (Name / Sangya)",
        optionB = "ᱠᱟᱹᱢᱤ (Verb / Kriya)",
        optionC = "ᱜᱩᱱ (Adjective / Visheshan)",
        optionD = "ᱩᱧᱩᱢ (Pronoun / Sarvanam)",
        correctOption = "A",
        explanation = "ᱡᱟᱦᱟᱸ ᱟᱹᱲᱟᱹ ᱛᱮ ᱡᱟᱦᱟᱸᱱ ᱡᱤᱱᱤᱥ, ᱦᱚᱲ, ᱡᱟᱭᱜᱟ ᱨᱮᱱᱟᱜ ᱧᱩᱛᱩᱢ ᱵᱟᱰᱟᱭᱚᱜ-ᱟ ᱚᱱᱟ ᱫᱚ 'ᱧᱩᱱᱩᱢ' (Noun) ᱠᱚ ᱢᱮᱛᱟᱜ-ᱟ᱾",
        scriptTag = "Ol Chiki & Hindi"
      ),
      QuestionEntity(
        setId = set3Id,
        questionNumber = 2,
        questionText = "ᱥᱟᱱᱛᱟᱲᱤ ᱛᱮ 'ᱩᱧᱩᱢ' (Pronoun) ᱨᱮ 'ᱟᱞᱟᱝ' (We two) ᱫᱚ ᱚᱠᱟ ᱞᱮᱠᱟᱱ ᱜᱚᱨᱚᱱ ᱠᱟᱱᱟ?\n('Alang' shabd kis vachan aur purush ke liye prayukt hota hai?)",
        optionA = "ᱢᱟᱲᱟᱝ ᱜᱚᱨᱚᱱ, ᱵᱟᱨ ᱜᱚᱴᱟᱝ (First Person, Dual inclusive)",
        optionB = "ᱥᱟᱢᱟᱝ ᱜᱚᱨᱚᱱ (Second Person)",
        optionC = "ᱥᱟᱺᱜᱤᱧ ᱜᱚᱨᱚᱱ (Third Person)",
        optionD = "ᱥᱟᱸᱜᱮ ᱜᱚᱴᱟᱝ (Plural)",
        correctOption = "A",
        explanation = "'ᱟᱞᱟᱝ' ᱫᱚ ᱵᱟᱨ ᱦᱚᱲ (Dual) ᱞᱟᱹᱜᱤᱫ ᱵᱮᱵᱷᱟᱨᱚᱜ-ᱟ, ᱡᱟᱦᱟᱸ ᱨᱮ ᱨᱚᱲᱤᱡ ᱟᱨ ᱟᱸᱡᱚᱢᱤᱡ ᱵᱟᱱᱟᱨ ᱠᱤᱱ ᱥᱮᱞᱮᱫ ᱛᱟᱦᱮᱸᱱᱟ᱾",
        scriptTag = "Ol Chiki & Hindi"
      ),
      QuestionEntity(
        setId = set3Id,
        questionNumber = 3,
        questionText = "ᱠᱩᱫᱩᱢ (Riddle) ᱨᱮᱱᱟᱜ ᱛᱮᱞᱟ ᱮᱢ ᱢᱮ: 'ᱢᱤᱫᱴᱟᱝ ᱜᱟᱹᱭ ᱫᱚ ᱜᱚᱴᱟ ᱦᱚᱲᱢᱚ ᱨᱮ ᱢᱮᱫ' - ᱱᱚᱣᱟ ᱫᱚ ᱪᱮᱫ ᱠᱟᱱᱟ?\n(Kudum ka uttar dein: 'Ek gai jiske pure sharir par aankhein hain')",
        optionA = "ᱡᱷᱟᱹᱞᱤ / ᱡᱟᱞᱟᱢ (Fishing Net)",
        optionB = "ᱟᱛᱷᱟ",
        optionC = "ᱢᱟᱫ",
        optionD = "ᱦᱟᱹᱠᱩ",
        correctOption = "A",
        explanation = "ᱡᱟᱞᱟᱢ (Net) ᱨᱮ ᱟᱹᱰᱤ ᱜᱟᱱ ᱵᱷᱩᱜᱟᱹᱜ ᱛᱟᱦᱮᱸᱱᱟ ᱡᱟᱦᱟᱸ ᱫᱚ ᱢᱮᱫ ᱞᱮᱠᱟ ᱧᱮᱞᱚᱜ-ᱟ᱾",
        scriptTag = "Ol Chiki & Hindi"
      ),
      QuestionEntity(
        setId = set3Id,
        questionNumber = 4,
        questionText = "ᱥᱟᱱᱛᱟᱲᱤ ᱛᱮ 'ᱠᱟᱱᱣᱟ' (Verb) ᱨᱮᱱᱟᱜ ᱢᱮᱱᱮᱛ ᱫᱚ ᱪᱮᱫ ᱠᱟᱱᱟ?\n(Santali Vyakaran mein 'Kanwa' kise kehte hain?)",
        optionA = "ᱠᱟᱹᱢᱤ ᱵᱩᱡᱷᱟᱹᱣᱜ ᱟᱹᱲᱟᱹ (Action / Kriya)",
        optionB = "ᱜᱩᱱ ᱵᱩᱡᱷᱟᱹᱣᱜ ᱟᱹᱲᱟᱹ",
        optionC = "ᱧᱩᱛᱩᱢ ᱵᱩᱡᱷᱟᱹᱣᱜ ᱟᱹᱲᱟᱹ",
        optionD = "ᱵᱚᱫᱚᱞ ᱛᱮ ᱦᱤᱡᱩᱜ ᱟᱹᱲᱟᱹ",
        correctOption = "A",
        explanation = "ᱡᱟᱦᱟᱸ ᱟᱹᱲᱟᱹ ᱛᱮ ᱪᱮᱫ ᱠᱟᱹᱢᱤ ᱦᱩᱭᱩᱜ ᱠᱟᱱᱟ ᱵᱟᱰᱟᱭᱚᱜ-ᱟ (ᱡᱮᱞᱮᱠᱟ - ᱪᱟᱞᱟᱜ, ᱡᱚᱢ, ᱚᱞ) ᱚᱱᱟ ᱫᱚ 'ᱠᱟᱱᱣᱟ' ᱠᱚ ᱢᱮᱛᱟᱜ-ᱟ᱾",
        scriptTag = "Ol Chiki & Hindi"
      ),
      QuestionEntity(
        setId = set3Id,
        questionNumber = 5,
        questionText = "ᱥᱟᱱᱛᱟᱲᱤ ᱛᱮ 'ᱡᱟᱱᱟᱝ' (Gender) ᱛᱤᱱᱟᱹᱜ ᱦᱟᱹᱴᱤᱧ ᱨᱮ ᱢᱮᱱᱟᱜ-ᱟ?\n(Santali bhasha mein Ling / Janang kitne prakar ke hote hain?)",
        optionA = "᱔ (Four - Kora, Kuri, Hatad, Jethat)",
        optionB = "᱒ (Two)",
        optionC = "᱓ (Three)",
        optionD = "᱕ (Five)",
        correctOption = "A",
        explanation = "ᱥᱟᱱᱛᱟᱲᱤ ᱨᱮ ᱔ ᱦᱟᱹᱴᱤᱧ ᱡᱟᱱᱟᱝ ᱢᱮᱱᱟᱜ-ᱟ: ᱠᱚᱲᱟ ᱡᱟᱱᱟᱝ (Masculine), ᱠᱩᱲᱤ ᱡᱟᱱᱟᱝ (Feminine), ᱡᱮᱛᱷᱟᱛ ᱡᱟᱱᱟᱝ (Common), ᱦᱟᱛᱟᱫ ᱡᱟᱱᱟᱝ (Neuter)᱾",
        scriptTag = "Ol Chiki & Hindi"
      ),
      QuestionEntity(
        setId = set3Id,
        questionNumber = 6,
        questionText = "'ᱠᱷᱮᱨᱣᱟᱲ ᱵᱤᱨ' (Kherwal Bir) ᱯᱩᱛᱷᱤ ᱫᱚ ᱚᱠᱚᱭᱟᱜ ᱚᱞ ᱠᱟᱱᱟ?\n('Kherwal Bir' pustak ke lekhak kaun hain?)",
        optionA = "ᱯᱚᱸᱰᱮᱛ ᱨᱟᱹᱜᱷᱩᱱᱟᱛᱷ ᱢᱩᱨᱢᱩ (Pandit Raghunath Murmu)",
        optionB = "ᱥᱟᱨᱫᱟ ᱯᱨᱚᱥᱟᱫᱽ ᱠᱤᱥᱠᱩ",
        optionC = "ᱵᱟᱫᱚᱞ ᱦᱮᱢᱵᱽᱨᱚᱢ",
        optionD = "ᱠᱨᱤᱥᱱᱚ ᱪᱚᱱᱫᱽᱨᱚ ᱴᱩᱰᱩ",
        correctOption = "A",
        explanation = "'ᱠᱷᱮᱨᱣᱟᱲ ᱵᱤᱨ' ᱫᱚ ᱜᱩᱨᱩ ᱜᱚᱢᱠᱮ ᱯᱚᱸᱰᱮᱛ ᱨᱟᱹᱜᱷᱩᱱᱟᱛᱷ ᱢᱩᱨᱢᱩ ᱛᱤᱠᱤᱱᱟᱜ ᱢᱤᱫ ᱧᱩᱛᱩᱢᱟᱱ ᱜᱟᱭᱟᱱ ᱯᱩᱛᱷᱤ ᱠᱟᱱᱟ᱾",
        scriptTag = "Ol Chiki & Hindi"
      ),
      QuestionEntity(
        setId = set3Id,
        questionNumber = 7,
        questionText = "ᱥᱟᱱᱛᱟᱲᱤ ᱛᱮ 'ᱦᱟᱯᱛᱟ' ᱨᱮᱱᱟᱜ ᱯᱩᱭᱞᱩ ᱢᱟᱦᱟᱸ 'ᱥᱤᱸᱜᱮ ᱢᱟᱦᱟᱸ' ᱢᱮᱱᱮᱛ ᱫᱚ ᱪᱮᱫ ᱦᱤᱞᱳᱜ ᱠᱟᱱᱟ?\n(Santali mein 'Singe Maha' ka arth kaun sa din hai?)",
        optionA = "ᱨᱚᱵᱤᱵᱟᱨ (Sunday)",
        optionB = "ᱥᱳᱢᱵᱟᱨ (Monday)",
        optionC = "ᱵᱩᱫᱷᱵᱟᱨ (Wednesday)",
        optionD = "ᱥᱩᱠᱨᱚᱵᱟᱨ (Friday)",
        correctOption = "A",
        explanation = "ᱥᱤᱸᱜᱮ ᱢᱟᱦᱟᱸ = Sunday (ᱨᱚᱵᱤᱵᱟᱨ), ᱚᱛᱮ ᱢᱟᱦᱟᱸ = Monday, ᱵᱟᱞᱮ ᱢᱟᱦᱟᱸ = Tuesday, ᱥᱟᱹᱜᱩᱱ ᱢᱟᱦᱟᱸ = Wednesday, ᱥᱟᱹᱨᱫᱤ ᱢᱟᱦᱟᱸ = Thursday, ᱡᱟᱹᱨᱩᱢ ᱢᱟᱦᱟᱸ = Friday, ᱧᱩᱦᱩᱢ ᱢᱟᱦᱟᱸ = Saturday᱾",
        scriptTag = "Ol Chiki & Hindi"
      ),
      QuestionEntity(
        setId = set3Id,
        questionNumber = 8,
        questionText = "ᱥᱟᱱᱛᱟᱲᱤ ᱛᱮ 'ᱮᱞ' (Number) ᱨᱮ ᱑᱐ (Ten) ᱫᱚ ᱪᱮᱫ ᱠᱚ ᱢᱮᱛᱟᱜ-ᱟ?\n(Santali ginti mein 10 ko kya kehte hain?)",
        optionA = "ᱜᱮᱞ (Gel)",
        optionB = "ᱢᱤᱫ (Mid)",
        optionC = "ᱢᱳᱬᱮ (More)",
        optionD = "ᱤᱥᱤ (Isi - 20)",
        correctOption = "A",
        explanation = "᱑ = ᱢᱤᱫ, ᱒ = ᱵᱟᱨ, ᱓ = ᱯᱮ, ᱔ = ᱯᱩᱱ, ᱕ = ᱢᱳᱬᱮ, ᱖ = ᱛᱩᱨᱩᱭ, ᱗ = ᱮᱭᱟᱭ, ᱘ = ᱤᱨᱟᱹᱞ, ᱙ = ᱟᱨᱮ, ᱑᱐ = ᱜᱮᱞ᱾",
        scriptTag = "Ol Chiki"
      ),
      QuestionEntity(
        setId = set3Id,
        questionNumber = 9,
        questionText = "'ᱜᱩᱨᱩ ᱜᱚᱢᱠᱮ' ᱢᱮᱱᱛᱮ ᱚᱠᱚᱭ ᱠᱚ ᱵᱟᱰᱟᱭᱮᱭᱟ?\n('Guru Gomke' ke naam se kise sammanit kiya gaya hai?)",
        optionA = "ᱯᱚᱸᱰᱮᱛ ᱨᱟᱹᱜᱷᱩᱱᱟᱛᱷ ᱢᱩᱨᱢᱩ",
        optionB = "ᱵᱤᱨᱥᱟ ᱢᱩᱱᱰᱟ",
        optionC = "ᱥᱤᱫᱷᱩ ᱢᱩᱨᱢᱩ",
        optionD = "ᱛᱤᱞᱠᱟᱹ ᱢᱟᱹᱡᱷᱤ",
        correctOption = "A",
        explanation = "ᱯᱚᱸᱰᱮᱛ ᱨᱟᱹᱜᱷᱩᱱᱟᱛᱷ ᱢᱩᱨᱢᱩ ᱛᱤᱠᱤᱱ ᱥᱟᱱᱛᱟᱲ ᱥᱟᱶᱛᱟ ᱨᱤᱱ ᱦᱚᱲ 'ᱜᱩᱨᱩ ᱜᱚᱢᱠᱮ' (The Great Teacher) ᱢᱮᱱᱛᱮ ᱠᱚ ᱢᱟᱱᱟᱣ ᱠᱤᱱᱟ᱾",
        scriptTag = "Ol Chiki & Hindi"
      ),
      QuestionEntity(
        setId = set3Id,
        questionNumber = 10,
        questionText = "ᱥᱟᱱᱛᱟᱲᱤ ᱨᱚᱱᱚᱲ ᱨᱮ 'ᱜᱩᱱᱩᱱ' (Adjective) ᱨᱮᱱᱟᱜ ᱫᱟᱹᱭᱠᱟᱹ (Example) ᱚᱠᱟ ᱠᱟᱱᱟ?\n(Santali mein 'Gunun' (Visheshan) ka udaharan kaun sa hai?)",
        optionA = "ᱦᱮᱸᱫᱮ ᱥᱟᱫᱚᱢ (Black Horse - 'hende')",
        optionB = "ᱫᱟᱹᱲ (Run)",
        optionC = "ᱫᱟᱨᱮ (Tree)",
        optionD = "ᱤᱧ (I / Me)",
        correctOption = "A",
        explanation = "'ᱦᱮᱸᱫᱮ' (Black / Kala) ᱫᱚ ᱥᱟᱫᱚᱢᱟᱜ ᱜᱩᱱ (Quality) ᱵᱩᱡᱷᱟᱹᱣᱮᱫ-ᱟ, ᱚᱱᱟᱛᱮ ᱱᱚᱣᱟ ᱫᱚ ᱜᱩᱱᱩᱱ (Adjective) ᱠᱟᱱᱟ᱾",
        scriptTag = "Ol Chiki & Hindi"
      )
    )
    questionDao.insertQuestions(questions3)
  }
}
