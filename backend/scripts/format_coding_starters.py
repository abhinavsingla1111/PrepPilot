#!/usr/bin/env python3
"""Format and validate the starter programs stored in coding-prompts.json."""

from __future__ import annotations

import json
import re
import subprocess
import tempfile
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
PROMPTS = ROOT / "src/main/resources/interview/coding-prompts.json"
CLANG_FORMAT = Path("/Library/Developer/CommandLineTools/usr/bin/clang-format")


TODO_MARKER = "// TODO: implement the interview solution."


def repair_one_wrapped_todo_comment(source: str) -> str:
    """Undo clang-format wrapping from an earlier line-comment conversion."""
    lines = source.splitlines()
    repaired: list[str] = []
    index = 0
    while index < len(lines):
        line = lines[index]
        if TODO_MARKER not in line:
            repaired.append(line)
            index += 1
            continue

        prefix, suffix = line.split(TODO_MARKER, 1)
        fragments = [suffix.strip()]
        index += 1
        while index < len(lines):
            wrapped = re.match(r"^\s*//\s?(.*)$", lines[index])
            if not wrapped:
                break
            fragments.append(wrapped.group(1).strip())
            index += 1
        repaired.append(f"{prefix}/* TODO: implement the interview solution. */ {' '.join(fragments)}")
    return "\n".join(repaired) + "\n"


def repair_wrapped_todo_comments(source: str) -> str:
    while TODO_MARKER in source:
        repaired = repair_one_wrapped_todo_comment(source)
        if repaired == source:
            break
        source = repaired
    return source


def format_source(source: str, filename: str) -> str:
    if not CLANG_FORMAT.is_file():
        raise SystemExit(f"clang-format was not found at {CLANG_FORMAT}")

    readable_source = repair_wrapped_todo_comments(source).replace(
        "/* TODO */", "/* TODO: implement the interview solution. */"
    )
    completed = subprocess.run(
        [
            str(CLANG_FORMAT),
            f"--assume-filename={filename}",
            "--style={BasedOnStyle: LLVM, IndentWidth: 2, "
            "ContinuationIndentWidth: 2, ColumnLimit: 100}",
        ],
        check=True,
        input=readable_source,
        text=True,
        capture_output=True,
    )
    return completed.stdout.rstrip() + "\n"


def validate_two_space_indentation(source: str, language: str, slug: str) -> None:
    """Reject tabs and indentation that cannot be represented in two-space steps."""
    for line_number, line in enumerate(source.splitlines(), start=1):
        if "\t" in line:
            raise ValueError(f"{slug} {language}:{line_number} contains a tab")
        leading_spaces = len(line) - len(line.lstrip(" "))
        if language == "python" and leading_spaces % 2 != 0:
            raise ValueError(
                f"{slug} {language}:{line_number} uses {leading_spaces} leading spaces"
            )


def validate_starters(prompts: list[dict]) -> None:
    """Compile every executable starter so formatting cannot silently break it."""
    with tempfile.TemporaryDirectory(prefix="preppilot-starters-") as directory:
        workspace = Path(directory)
        for prompt in prompts:
            slug = prompt["slug"]
            for language, source in prompt["starters"].items():
                validate_two_space_indentation(source, language, slug)

            java_dir = workspace / f"{slug}-java"
            java_dir.mkdir()
            java_file = java_dir / "Main.java"
            java_file.write_text(prompt["starters"]["java"], encoding="utf-8")
            subprocess.run(
                ["javac", str(java_file)], check=True, capture_output=True, text=True
            )

            cpp_file = workspace / f"{slug}.cpp"
            cpp_file.write_text(prompt["starters"]["cpp"], encoding="utf-8")
            subprocess.run(
                ["c++", "-std=c++17", "-fsyntax-only", str(cpp_file)],
                check=True,
                capture_output=True,
                text=True,
            )

            compile(prompt["starters"]["python"], f"{slug}.py", "exec")


def main() -> None:
    prompts = json.loads(PROMPTS.read_text(encoding="utf-8"))
    for prompt in prompts:
        prompt["starters"]["java"] = format_source(prompt["starters"]["java"], "Main.java")
        prompt["starters"]["cpp"] = format_source(prompt["starters"]["cpp"], "main.cpp")

    validate_starters(prompts)
    PROMPTS.write_text(json.dumps(prompts, indent=2) + "\n", encoding="utf-8")


if __name__ == "__main__":
    main()
