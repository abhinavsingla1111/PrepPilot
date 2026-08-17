package com.preppilot.api.assessment;

import com.preppilot.api.problem.Difficulty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AssessmentQuestionSeedService {

    public static final int QUESTIONS_PER_TOPIC = 100;
    public static final int TOTAL_QUESTIONS = AssessmentTopic.values().length * QUESTIONS_PER_TOPIC;

    private static final Pattern HEADER_PATTERN = Pattern.compile(
            "^\\*\\*Q(\\d+)\\. \\[(Easy|Medium|Hard)](?: \\((True/False|One word)\\))?\\*\\*\\s*(.*)$"
    );
    private static final Pattern OPTION_PATTERN = Pattern.compile("^- ([A-D])\\)\\s*(.*)$");
    private static final String ANSWER_PREFIX = "**Answer:**";

    private static final Map<QuestionKey, List<String>> SHORT_ANSWER_DISTRACTORS = Map.ofEntries(
            // Object-oriented programming
            entry(AssessmentTopic.OOPS, 8, "Factory", "Destructor", "Builder"),
            entry(AssessmentTopic.OOPS, 20, "Overriding", "Hiding", "Shadowing"),
            entry(AssessmentTopic.OOPS, 25, "Abstraction", "Inheritance", "Polymorphism"),
            entry(AssessmentTopic.OOPS, 34, "Declaration", "Generalization", "Composition"),
            entry(AssessmentTopic.OOPS, 56, "Injection", "Delegation", "Separation"),
            entry(AssessmentTopic.OOPS, 70, "Final", "Static", "Singleton"),
            entry(AssessmentTopic.OOPS, 90, "Contravariance", "Invariance", "Bivariance"),

            // Java
            entry(AssessmentTopic.JAVA, 3, "JDK", "JRE", "JIT"),
            entry(AssessmentTopic.JAVA, 13, "static", "sealed", "private"),
            entry(AssessmentTopic.JAVA, 20, "Deserialization", "Marshalling", "Reflection"),
            entry(AssessmentTopic.JAVA, 28, "volatile", "const", "immutable"),
            entry(AssessmentTopic.JAVA, 34, "java", "javadoc", "javap"),
            entry(AssessmentTopic.JAVA, 50, "Override", "SafeVarargs", "Retention"),
            entry(AssessmentTopic.JAVA, 63, "LongAdder", "Concurrent", "Synchronized"),
            entry(AssessmentTopic.JAVA, 70, "clone", "compareTo", "finalize"),
            entry(AssessmentTopic.JAVA, 83, "Overload", "Hide", "Synchronize"),
            entry(AssessmentTopic.JAVA, 94, "Adapter", "Proxy", "Facade"),

            // Spring Boot
            entry(AssessmentTopic.SPRING_BOOT, 3, "ImportAuto", "AutoScan", "EnableWeb"),
            entry(AssessmentTopic.SPRING_BOOT, 9, "Jetty", "Undertow", "Netty"),
            entry(AssessmentTopic.SPRING_BOOT, 15, "junit", "mockito", "testing"),
            entry(AssessmentTopic.SPRING_BOOT, 21, "Param", "Header", "Part"),
            entry(AssessmentTopic.SPRING_BOOT, 27, "Crud", "Entity", "Paging"),
            entry(AssessmentTopic.SPRING_BOOT, 33, "201", "204", "302"),
            entry(AssessmentTopic.SPRING_BOOT, 37, "factories", "metadata", "properties"),
            entry(AssessmentTopic.SPRING_BOOT, 50, "Gson", "Moshi", "Jsonb"),
            entry(AssessmentTopic.SPRING_BOOT, 58, "Spy", "Test", "Stub"),
            entry(AssessmentTopic.SPRING_BOOT, 66, "Adapters", "Decorators", "Interceptors"),
            entry(AssessmentTopic.SPRING_BOOT, 73, "mvc", "reactor", "netty"),
            entry(AssessmentTopic.SPRING_BOOT, 82, "Request", "Transaction", "Controller"),
            entry(AssessmentTopic.SPRING_BOOT, 89, "Bean", "Definition", "Registry"),
            entry(AssessmentTopic.SPRING_BOOT, 95, "Vault", "Gateway", "Discovery"),

            // React
            entry(AssessmentTopic.REACT, 3, "Shadow DOM", "Browser DOM", "Document Fragment"),
            entry(AssessmentTopic.REACT, 10, "Element", "Hook", "Reducer"),
            entry(AssessmentTopic.REACT, 15, "filter", "reduce", "forEach"),
            entry(AssessmentTopic.REACT, 21, "State", "Memo", "Effect"),
            entry(AssessmentTopic.REACT, 27, "Container", "Controlled", "Higher-order"),
            entry(AssessmentTopic.REACT, 33, "Lifting", "Bubbling", "Hoisting"),
            entry(AssessmentTopic.REACT, 43, "Component", "Reducer", "Context"),
            entry(AssessmentTopic.REACT, 50, "Callback", "Effect", "Reducer"),
            entry(AssessmentTopic.REACT, 56, "Mounted", "Hydrated", "Committed"),
            entry(AssessmentTopic.REACT, 63, "Selectors", "Actions", "Middleware"),
            entry(AssessmentTopic.REACT, 69, "Presentational", "Controlled", "Pure"),
            entry(AssessmentTopic.REACT, 77, "Transition", "Layout", "SyncExternalStore"),
            entry(AssessmentTopic.REACT, 83, "Actions", "Hooks", "Boundaries"),
            entry(AssessmentTopic.REACT, 89, "Pagination", "Memoization", "Hydration"),
            entry(AssessmentTopic.REACT, 95, "Commit", "Layout", "Passive"),

            // C++
            entry(AssessmentTopic.CPP, 3, "malloc", "create", "alloc"),
            entry(AssessmentTopic.CPP, 8, "Destructor", "Initializer", "Factory"),
            entry(AssessmentTopic.CPP, 14, "constexpr", "static", "virtual"),
            entry(AssessmentTopic.CPP, 20, "virtual", "sealed", "incomplete"),
            entry(AssessmentTopic.CPP, 25, "static", "define", "literal"),
            entry(AssessmentTopic.CPP, 31, "raise", "except", "error"),
            entry(AssessmentTopic.CPP, 44, "Instantiation", "Inheritance", "Isolation"),
            entry(AssessmentTopic.CPP, 52, "Instantiation", "Deduction", "Overload"),
            entry(AssessmentTopic.CPP, 58, "decltype", "typename", "using"),
            entry(AssessmentTopic.CPP, 65, "2", "log n", "n²"),
            entry(AssessmentTopic.CPP, 71, "typename", "class", "concept"),
            entry(AssessmentTopic.CPP, 81, "Coroutines", "Modules", "Ranges"),
            entry(AssessmentTopic.CPP, 87, "move", "assign", "exchange"),
            entry(AssessmentTopic.CPP, 93, "argument", "reference", "concept"),
            entry(AssessmentTopic.CPP, 99, "fence", "barrier", "sequence"),

            // Python
            entry(AssessmentTopic.PYTHON, 3, "list", "set", "deque"),
            entry(AssessmentTopic.PYTHON, 9, "null", "Nil", "False"),
            entry(AssessmentTopic.PYTHON, 15, "module", "class", "generator"),
            entry(AssessmentTopic.PYTHON, 21, "def", "yield", "anon"),
            entry(AssessmentTopic.PYTHON, 27, "CLI", "IDE", "PIP"),
            entry(AssessmentTopic.PYTHON, 33, "read", "scan", "stdin"),
            entry(AssessmentTopic.PYTHON, 43, "deepcopy", "pickle", "clone"),
            entry(AssessmentTopic.PYTHON, 50, "str", "format", "print"),
            entry(AssessmentTopic.PYTHON, 56, "context", "manager", "closing"),
            entry(AssessmentTopic.PYTHON, 62, "type", "object", "struct"),
            entry(AssessmentTopic.PYTHON, 69, "containers", "itertools", "datatypes"),
            entry(AssessmentTopic.PYTHON, 75, "nonlocal", "extern", "module"),
            entry(AssessmentTopic.PYTHON, 81, "mor", "resolve", "order"),
            entry(AssessmentTopic.PYTHON, 87, "protocol", "descriptor", "decorator"),
            entry(AssessmentTopic.PYTHON, 93, "immutable", "comparable", "serializable"),
            entry(AssessmentTopic.PYTHON, 99, "invoke", "apply", "run"),

            // Computer networks
            entry(AssessmentTopic.COMPUTER_NETWORKS, 2, "Transport", "Data Link", "Session"),
            entry(AssessmentTopic.COMPUTER_NETWORKS, 8, "Switch", "Bridge", "Gateway"),
            entry(AssessmentTopic.COMPUTER_NETWORKS, 15, "DNS", "ARP", "ICMP"),
            entry(AssessmentTopic.COMPUTER_NETWORKS, 21, "Frame", "Segment", "Datagram"),
            entry(AssessmentTopic.COMPUTER_NETWORKS, 27, "traceroute", "nslookup", "netstat"),
            entry(AssessmentTopic.COMPUTER_NETWORKS, 33, "Mesh", "Ring", "Bus"),
            entry(AssessmentTopic.COMPUTER_NETWORKS, 38, "MTU", "TOS", "MSS"),
            entry(AssessmentTopic.COMPUTER_NETWORKS, 46, "AAAA", "CNAME", "PTR"),
            entry(AssessmentTopic.COMPUTER_NETWORKS, 52, "Congestion", "Send", "Sliding"),
            entry(AssessmentTopic.COMPUTER_NETWORKS, 58, "Success", "Client error", "Server error"),
            entry(AssessmentTopic.COMPUTER_NETWORKS, 64, "BGP", "RIP", "EIGRP"),
            entry(AssessmentTopic.COMPUTER_NETWORKS, 70, "MAC", "Port", "Host"),
            entry(AssessmentTopic.COMPUTER_NETWORKS, 81, "Delay", "Drop", "Detection"),
            entry(AssessmentTopic.COMPUTER_NETWORKS, 87, "MTU", "TTL", "RTT"),
            entry(AssessmentTopic.COMPUTER_NETWORKS, 93, "SNAT", "DNAT", "CGNAT"),
            entry(AssessmentTopic.COMPUTER_NETWORKS, 99, "TLS", "SIG", "KEY"),

            // MySQL
            entry(AssessmentTopic.MYSQL, 3, "DELETE", "DROP", "REMOVE"),
            entry(AssessmentTopic.MYSQL, 8, "UNIQUE", "GROUP", "FILTER"),
            entry(AssessmentTopic.MYSQL, 14, "SUM", "TOTAL", "TALLY"),
            entry(AssessmentTopic.MYSQL, 20, "MyISAM", "Memory", "Aria"),
            entry(AssessmentTopic.MYSQL, 26, "TOP", "ROWNUM", "FETCH"),
            entry(AssessmentTopic.MYSQL, 32, "Primary", "Unique", "Composite"),
            entry(AssessmentTopic.MYSQL, 38, "INNER", "OUTER", "NATURAL"),
            entry(AssessmentTopic.MYSQL, 46, "bookmark", "index", "key"),
            entry(AssessmentTopic.MYSQL, 53, "Consistency", "Isolation", "Durability"),
            entry(AssessmentTopic.MYSQL, 59, "Index", "Trigger", "Procedure"),
            entry(AssessmentTopic.MYSQL, 65, "SKIP", "START", "PAGE"),
            entry(AssessmentTopic.MYSQL, 71, "partial", "functional", "multivalued"),
            entry(AssessmentTopic.MYSQL, 78, "gap", "record", "insert"),
            entry(AssessmentTopic.MYSQL, 85, "ROW_NUMBER", "DENSE_RANK", "NTILE"),
            entry(AssessmentTopic.MYSQL, 92, "phantom", "dirty", "committed"),
            entry(AssessmentTopic.MYSQL, 98, "cache", "page", "index"),

            // Operating systems
            entry(AssessmentTopic.OPERATING_SYSTEMS, 2, "Thread", "Job", "Task"),
            entry(AssessmentTopic.OPERATING_SYSTEMS, 8, "address", "space", "page"),
            entry(AssessmentTopic.OPERATING_SYSTEMS, 14, "Buffer", "Base", "Boundary"),
            entry(AssessmentTopic.OPERATING_SYSTEMS, 20, "strap", "sector", "manager"),
            entry(AssessmentTopic.OPERATING_SYSTEMS, 26, "Page", "Segment", "Block"),
            entry(AssessmentTopic.OPERATING_SYSTEMS, 32, "PC", "PPID", "UID"),
            entry(AssessmentTopic.OPERATING_SYSTEMS, 38, "Waiting", "Response", "Burst"),
            entry(AssessmentTopic.OPERATING_SYSTEMS, 45, "Semaphore", "Monitor", "Spinlock"),
            entry(AssessmentTopic.OPERATING_SYSTEMS, 53, "FIFO", "LFU", "Optimal"),
            entry(AssessmentTopic.OPERATING_SYSTEMS, 59, "Contention", "Inversion", "Thrashing"),
            entry(AssessmentTopic.OPERATING_SYSTEMS, 65, "short", "medium", "real"),
            entry(AssessmentTopic.OPERATING_SYSTEMS, 71, "STOP", "TERM", "INT"),
            entry(AssessmentTopic.OPERATING_SYSTEMS, 81, "Deadlock", "Starvation", "Thrashing"),
            entry(AssessmentTopic.OPERATING_SYSTEMS, 87, "FIFO", "LFU", "Optimal"),
            entry(AssessmentTopic.OPERATING_SYSTEMS, 93, "fork", "spawn", "wait"),
            entry(AssessmentTopic.OPERATING_SYSTEMS, 99, "SJF", "Priority", "SRTF")
    );

    private static final List<Source> SOURCES = List.of(
            new Source(AssessmentTopic.OOPS, "assessments/questions/oops.md"),
            new Source(AssessmentTopic.JAVA, "assessments/questions/java.md"),
            new Source(AssessmentTopic.SPRING_BOOT, "assessments/questions/spring-boot.md"),
            new Source(AssessmentTopic.REACT, "assessments/questions/react.md"),
            new Source(AssessmentTopic.CPP, "assessments/questions/cpp.md"),
            new Source(AssessmentTopic.PYTHON, "assessments/questions/python.md"),
            new Source(AssessmentTopic.COMPUTER_NETWORKS, "assessments/questions/computer-networks.md"),
            new Source(AssessmentTopic.MYSQL, "assessments/questions/mysql.md"),
            new Source(AssessmentTopic.OPERATING_SYSTEMS, "assessments/questions/operating-systems.md")
    );

    private final AssessmentQuestionRepository questionRepository;

    public AssessmentQuestionSeedService(AssessmentQuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Transactional
    public void seedIfRequired() {
        for (Source source : SOURCES) {
            List<RawQuestion> rawQuestions = parse(source);
            validateDistribution(source.topic(), rawQuestions);

            List<AssessmentQuestion> missing = new ArrayList<>();
            for (RawQuestion raw : rawQuestions) {
                AssessmentQuestion desired = toEntity(source.topic(), raw);
                questionRepository.findByTopicAndSourceNumber(source.topic(), raw.number())
                        .ifPresentOrElse(
                                existing -> existing.refreshContent(
                                        desired.getDifficulty(),
                                        desired.getPrompt(),
                                        desired.getOptions(),
                                        desired.getCorrectOption(),
                                        desired.getJustification()
                                ),
                                () -> missing.add(desired)
                        );
            }
            questionRepository.saveAll(missing);
        }

        if (questionRepository.count() != TOTAL_QUESTIONS) {
            throw new IllegalStateException("Expected exactly " + TOTAL_QUESTIONS + " assessment questions.");
        }
    }

    private List<RawQuestion> parse(Source source) {
        try {
            String content = new ClassPathResource(source.resource()).getContentAsString(StandardCharsets.UTF_8);
            List<RawQuestion> questions = new ArrayList<>();
            RawQuestionBuilder current = null;

            for (String line : content.split("\\R", -1)) {
                Matcher header = HEADER_PATTERN.matcher(line);
                if (header.matches()) {
                    if (current != null) questions.add(current.build());
                    current = new RawQuestionBuilder(
                            Integer.parseInt(header.group(1)),
                            Difficulty.valueOf(header.group(2).toUpperCase(Locale.ROOT)),
                            QuestionKind.fromLabel(header.group(3))
                    );
                    current.addPromptLine(header.group(4));
                    continue;
                }

                if (current == null) continue;

                Matcher option = OPTION_PATTERN.matcher(line);
                if (option.matches()) {
                    current.addOption(option.group(2).trim());
                    continue;
                }

                if (line.startsWith(ANSWER_PREFIX)) {
                    current.setAnswer(line.substring(ANSWER_PREFIX.length()).trim());
                    continue;
                }

                current.addPromptLine(line);
            }

            if (current != null) questions.add(current.build());
            return questions;
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read assessment source " + source.resource(), exception);
        }
    }

    private AssessmentQuestion toEntity(
            AssessmentTopic topic,
            RawQuestion raw
    ) {
        List<String> options;
        int correctOption;

        if (raw.kind() == QuestionKind.MULTIPLE_CHOICE) {
            if (raw.options().size() != 4 || !raw.answer().matches("[A-D]")) {
                throw malformed(topic, raw.number(), "expected four options and an A-D answer");
            }
            options = raw.options();
            correctOption = raw.answer().charAt(0) - 'A';
        } else if (raw.kind() == QuestionKind.TRUE_FALSE) {
            String correct = normalizeTrueFalse(raw.answer(), topic, raw.number());
            options = new ArrayList<>(List.of("True", "False", "Cannot be determined", "Not applicable"));
            Collections.rotate(options, raw.number() % options.size());
            correctOption = options.indexOf(correct);
        } else {
            options = generatedShortAnswerOptions(topic, raw);
            correctOption = options.indexOf(raw.answer());
        }

        return new AssessmentQuestion(
                topic,
                raw.number(),
                raw.difficulty(),
                raw.prompt(),
                options,
                correctOption,
                raw.justification()
        );
    }

    private List<String> generatedShortAnswerOptions(
            AssessmentTopic topic,
            RawQuestion raw
    ) {
        List<String> curated = SHORT_ANSWER_DISTRACTORS.get(new QuestionKey(topic, raw.number()));
        if (curated == null) {
            throw new IllegalStateException(
                    "Missing curated distractors for " + topic + " one-word question Q" + raw.number()
            );
        }
        return combineAndRotate(raw, curated);
    }

    private List<String> combineAndRotate(RawQuestion raw, List<String> distractors) {
        if (distractors.size() != 3 || new LinkedHashSet<>(distractors).size() != 3
                || distractors.stream().anyMatch(value -> value.equalsIgnoreCase(raw.answer()))) {
            throw new IllegalStateException("Invalid curated distractors for one-word question Q" + raw.number());
        }
        List<String> options = new ArrayList<>(distractors);
        options.add(raw.number() % 4, raw.answer());
        return List.copyOf(options);
    }

    private static Map.Entry<QuestionKey, List<String>> entry(
            AssessmentTopic topic,
            int sourceNumber,
            String first,
            String second,
            String third
    ) {
        return Map.entry(new QuestionKey(topic, sourceNumber), List.of(first, second, third));
    }

    private String normalizeTrueFalse(String answer, AssessmentTopic topic, int number) {
        if (answer.equalsIgnoreCase("true")) return "True";
        if (answer.equalsIgnoreCase("false")) return "False";
        throw malformed(topic, number, "expected a True or False answer");
    }

    private void validateDistribution(AssessmentTopic topic, List<RawQuestion> questions) {
        long easy = questions.stream().filter(question -> question.difficulty() == Difficulty.EASY).count();
        long medium = questions.stream().filter(question -> question.difficulty() == Difficulty.MEDIUM).count();
        long hard = questions.stream().filter(question -> question.difficulty() == Difficulty.HARD).count();
        if (questions.size() != 100 || easy != 35 || medium != 40 || hard != 25) {
            throw new IllegalStateException(
                    topic + " must contain 100 questions split 35 easy, 40 medium, and 25 hard."
            );
        }
    }

    private IllegalStateException malformed(AssessmentTopic topic, int number, String detail) {
        return new IllegalStateException("Malformed " + topic + " assessment question Q" + number + ": " + detail);
    }

    private record Source(AssessmentTopic topic, String resource) {
    }

    private record QuestionKey(AssessmentTopic topic, int sourceNumber) {
    }

    private record RawQuestion(
            int number,
            Difficulty difficulty,
            QuestionKind kind,
            String prompt,
            List<String> options,
            String answer,
            String justification
    ) {
    }

    private static final class RawQuestionBuilder {
        private final int number;
        private final Difficulty difficulty;
        private final QuestionKind kind;
        private final List<String> promptLines = new ArrayList<>();
        private final List<String> options = new ArrayList<>();
        private String answer;
        private String justification;
        private boolean optionsStarted;

        private RawQuestionBuilder(int number, Difficulty difficulty, QuestionKind kind) {
            this.number = number;
            this.difficulty = difficulty;
            this.kind = kind;
        }

        private void addPromptLine(String line) {
            if (!optionsStarted && answer == null) promptLines.add(line);
        }

        private void addOption(String option) {
            optionsStarted = true;
            options.add(option);
        }

        private void setAnswer(String answerLine) {
            int separator = answerLine.indexOf('—');
            if (separator < 0) throw new IllegalStateException("Question Q" + number + " is missing an answer justification.");
            this.answer = answerLine.substring(0, separator).trim();
            this.justification = answerLine.substring(separator + 1).trim();
        }

        private RawQuestion build() {
            if (answer == null || justification == null) {
                throw new IllegalStateException("Question Q" + number + " is missing its answer.");
            }
            String prompt = String.join("\n", promptLines).strip();
            if (prompt.isBlank()) throw new IllegalStateException("Question Q" + number + " has an empty prompt.");
            return new RawQuestion(number, difficulty, kind, prompt, List.copyOf(options), answer, justification);
        }
    }

    private enum QuestionKind {
        MULTIPLE_CHOICE,
        TRUE_FALSE,
        ONE_WORD;

        static QuestionKind fromLabel(String label) {
            if (label == null) return MULTIPLE_CHOICE;
            return label.equals("True/False") ? TRUE_FALSE : ONE_WORD;
        }
    }

}
