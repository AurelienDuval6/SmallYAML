def generate_big_yaml(filename="big.yaml", num_entries=1_500_000):
    """Generate a very large YAML file."""
    with open(filename, "w", encoding="utf-8") as f:
        f.write("data:\n")
        for i in range(1, num_entries + 1):
            f.write(f"  - id: {i}\n")
            f.write(f"    name: item_{i}\n")
            f.write(f"    value: {i * 7}\n")
            f.write(f"    status: {'active' if i % 2 == 0 else 'inactive'}\n")
            f.write(f"    category: category_{i % 100}\n")
            f.write("    tags:\n")
            for t in range(1, 6):
                f.write(f"      - tag_{t}\n")

if __name__ == "__main__":
    generate_big_yaml("big.yaml", num_entries=1_500_000)
    print("✅ Done! File 'big.yaml' created.")